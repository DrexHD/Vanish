package me.drex.vanish.util;

import com.mojang.authlib.GameProfile;
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
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;

import java.util.Collections;
import java.util.UUID;

public class VanishManager {

    public static final PlayerDataStorage<VanishData> VANISH_DATA_STORAGE = new JsonDataStorage<>("vanish", VanishData.class);

    public static void init() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            if (ConfigManager.vanish().actionBar) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    if (isVanished(player)) {
                        player.sendSystemMessage(Component.translatable("text.vanish.general.vanished"), true);
                    }
                }
            }
        });
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
            if (isVanished(sender) && ConfigManager.vanish().disableChat) {
                sender.sendSystemMessage(Component.translatable("text.vanish.chat.disabled").withStyle(ChatFormatting.RED));
                return false;
            } else {
                return true;
            }
        });
        ServerMessageEvents.ALLOW_COMMAND_MESSAGE.register((message, source, params) -> {
            ServerPlayer sender = source.getPlayer();
            if (sender != null) {
                if (isVanished(sender) && ConfigManager.vanish().disableChat) {
                    sender.sendSystemMessage(Component.translatable("text.vanish.chat.disabled").withStyle(ChatFormatting.RED));
                    return false;
                }
                return true;
            }
            return true;
        });
        PlayerDataApi.register(VANISH_DATA_STORAGE);
    }

    public static boolean isVanished(Entity entity) {
        if (entity instanceof VanishedEntity vanishedEntity) {
            return vanishedEntity.vanish$isVanished();
        }
        return false;
    }

    public static boolean isVanished(MinecraftServer server, UUID uuid) {
        VanishData data = PlayerDataApi.getCustomDataFor(server, uuid, VANISH_DATA_STORAGE);
        return data != null && data.vanished;
    }

    public static boolean canViewVanished(SharedSuggestionProvider observer) {
        return Permissions.check(observer, "vanish.feature.view", 2);
    }

    public static boolean canSeePlayer(ServerPlayer actor, CommandSourceStack observer) {
        if (isVanished(actor)) {
            if (observer.getEntity() != null && actor.equals(observer.getEntity())) {
                return true;
            } else {
                return canViewVanished(observer);
            }
        } else {
            return true;
        }
    }

    public static boolean canSeePlayer(MinecraftServer server, UUID actor, CommandSourceStack observer) {
        if (isVanished(server, actor)) {
            if (observer.getEntity() != null && actor.equals(observer.getEntity().getUUID())) {
                return true;
            } else {
                return canViewVanished(observer);
            }
        } else {
            return true;
        }
    }

    public static boolean setVanished(GameProfile profile, MinecraftServer server, boolean vanish) {
        if (isVanished(server, profile.getId()) == vanish) return false;
        ServerPlayer player = server.getPlayerList().getPlayer(profile.getId());
        boolean isOnline = player != null;
        if (vanish && isOnline) {
            vanish(player);
        }
        VanishData data = PlayerDataApi.getCustomDataFor(server, profile.getId(), VANISH_DATA_STORAGE);
        if (data == null) data = new VanishData();
        data.vanished = vanish;
        PlayerDataApi.setCustomDataFor(server, profile.getId(), VANISH_DATA_STORAGE, data);
        if (isOnline) {
            ((VanishedEntity) player).vanish$setDirty();
        }
        if (!vanish && isOnline) {
            unVanish(player);
        }
        if (isOnline) {
            server.invalidateStatus();
            VanishEvents.VANISH_EVENT.invoker().onVanish(player, vanish);
        }
        return true;
    }

    private static void unVanish(ServerPlayer actor) {
        PlayerList list = actor.level().getServer().getPlayerList();
        broadcastToOthers(actor, ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(Collections.singletonList(actor)));
        if (ConfigManager.vanish().sendJoinDisconnectMessage) {
            list.broadcastSystemMessage(VanishEvents.UN_VANISH_MESSAGE_EVENT.invoker().getUnVanishMessage(actor), false);
        }
    }

    private static void vanish(ServerPlayer actor) {
        PlayerList list = actor.level().getServer().getPlayerList();
        broadcastToOthers(actor, new ClientboundPlayerInfoRemovePacket(Collections.singletonList(actor.getUUID())));
        if (ConfigManager.vanish().sendJoinDisconnectMessage) {
            list.broadcastSystemMessage(VanishEvents.VANISH_MESSAGE_EVENT.invoker().getVanishMessage(actor), false);
        }
    }

    private static void broadcastToOthers(ServerPlayer actor, Packet<?> packet) {
        for (ServerPlayer observer : actor.level().getServer().getPlayerList().getPlayers()) {
            if (!VanishAPI.canViewVanished(observer.createCommandSourceStack()) && !observer.equals(actor)) {
                observer.connection.send(packet);
            }
        }
    }
}