package me.drex.vanish.util;

import eu.pb4.playerdata.api.PlayerDataApi;
import eu.pb4.playerdata.api.storage.JsonDataStorage;
import eu.pb4.playerdata.api.storage.PlayerDataStorage;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.api.VanishEvents;
import me.drex.vanish.config.ConfigManager;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

import java.util.UUID;

public class VanishManager {

    public static final PlayerDataStorage<VanishData> VANISH_DATA_STORAGE = new JsonDataStorage<>("vanish", VanishData.class);

    public static void init() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            if (ConfigManager.INSTANCE.vanish().actionBar)
                server.getPlayerList().getPlayers().stream().filter(VanishAPI::isVanished)
                        .forEach(serverPlayer -> serverPlayer.sendSystemMessage(Component.translatable("text.vanish.general.vanished"), true));
        });
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
            if (VanishAPI.isVanished(sender)) {
                sender.sendSystemMessage(Component.translatable("text.vanish.chat.disabled").withStyle(ChatFormatting.RED));
                return false;
            } else {
                return true;
            }
        });
        ServerMessageEvents.ALLOW_COMMAND_MESSAGE.register((message, source, params) -> {
            ServerPlayer sender = source.getPlayer();
            if (sender != null) {
                if (VanishAPI.isVanished(sender)) {
                    sender.sendSystemMessage(Component.translatable("text.vanish.chat.disabled").withStyle(ChatFormatting.RED));
                    return false;
                }
                return true;
            }
            return true;
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
        list.broadcastSystemMessage(VanishEvents.UN_VANISH_MESSAGE_EVENT.invoker().getUnVanishMessage(vanisher), false);
    }

    private static void vanish(ServerPlayer vanisher) {
        PlayerList list = vanisher.server.getPlayerList();
        broadcastToOthers(vanisher, new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, vanisher));
        list.broadcastSystemMessage(VanishEvents.VANISH_MESSAGE_EVENT.invoker().getVanishMessage(vanisher), false);
    }

    private static void broadcastToOthers(ServerPlayer vanisher, Packet<?> packet) {
        vanisher.server.getPlayerList().getPlayers().stream()
                .filter(viewer -> !canViewVanished(viewer) && !viewer.equals(vanisher))
                .forEach(viewer -> {
                    viewer.connection.send(packet);
                });
    }

}
