package me.drex.vanish.util;

import eu.pb4.playerdata.api.PlayerDataApi;
import eu.pb4.playerdata.api.storage.JsonDataStorage;
import eu.pb4.playerdata.api.storage.PlayerDataStorage;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.api.VanishEvents;
import me.drex.vanish.config.ConfigManager;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

import java.util.UUID;

public class VanishManager {

    public static final PlayerDataStorage<VanishData> VANISH_DATA_STORAGE = new JsonDataStorage<>("vanish", VanishData.class);

    public static void init() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
          if (ConfigManager.INSTANCE.vanish().actionBar) {
              for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                  if (VanishAPI.isVanished(player)) {
                      player.sendMessage(new TranslatableComponent("text.vanish.general.vanished"), ChatType.GAME_INFO, Util.NIL_UUID);
                  }
              }
          }
        });
        PlayerDataApi.register(VANISH_DATA_STORAGE);
    }

    public static boolean isVanished(MinecraftServer server, UUID uuid) {
        VanishData data = PlayerDataApi.getCustomDataFor(server, uuid, VANISH_DATA_STORAGE);
        return data != null && data.vanished;
    }

    public static boolean canViewVanished(ServerPlayer player) {
        return canViewVanished(player.createCommandSourceStack());
    }

    public static boolean canViewVanished(SharedSuggestionProvider src) {
        return Permissions.check(src, "vanish.feature.view", 2);
    }

    public static boolean canSeePlayer(MinecraftServer server, UUID executive, CommandSourceStack viewer) {
        if (isVanished(server, executive)) {
            if (viewer.getEntity() != null && executive.equals(viewer.getEntity().getUUID())) {
                return true;
            } else {
                return canViewVanished(viewer);
            }
        } else {
            return true;
        }
    }

    public static boolean setVanished(ServerPlayer vanisher, boolean vanish) {
        if (isVanished(vanisher.server, vanisher.getUUID()) == vanish) return false;
        if (vanish) vanish(vanisher);
        VanishData data = PlayerDataApi.getCustomDataFor(vanisher, VANISH_DATA_STORAGE);
        if (data == null) data = new VanishData();
        data.vanished = vanish;
        PlayerDataApi.setCustomDataFor(vanisher, VANISH_DATA_STORAGE, data);
        if (!vanish) unVanish(vanisher);
        vanisher.server.invalidateStatus();
        VanishEvents.VANISH_EVENT.invoker().onVanish(vanisher, vanish);
        return true;
    }

    private static void unVanish(ServerPlayer vanisher) {
        PlayerList list = vanisher.server.getPlayerList();
        broadcastToOthers(vanisher, new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, vanisher));
        list.broadcastMessage(VanishEvents.UN_VANISH_MESSAGE_EVENT.invoker().getUnVanishMessage(vanisher), ChatType.SYSTEM, Util.NIL_UUID);
    }

    private static void vanish(ServerPlayer vanisher) {
        PlayerList list = vanisher.server.getPlayerList();
        broadcastToOthers(vanisher, new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, vanisher));
        list.broadcastMessage(VanishEvents.VANISH_MESSAGE_EVENT.invoker().getVanishMessage(vanisher), ChatType.SYSTEM, Util.NIL_UUID);
    }

    private static void broadcastToOthers(ServerPlayer vanisher, Packet<?> packet) {
        for (ServerPlayer viewer : vanisher.server.getPlayerList().getPlayers()) {
            if (!canViewVanished(viewer) && !viewer.equals(vanisher)) {
                viewer.connection.send(packet);
            }
        }
    }

}
