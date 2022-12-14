package me.drex.vanish.compat;

import de.bluecolored.bluemap.api.BlueMapAPI;
import eu.pb4.styledchat.StyledChatStyles;
import me.drex.vanish.api.VanishEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class ModCompat {

    public static final boolean STYLED_CHAT = FabricLoader.getInstance().isModLoaded("styledchat");
    public static final boolean BLUEMAP = FabricLoader.getInstance().isModLoaded("bluemap");
    public static boolean blueMapEventsRegistered = false;

    public static void init() {
        // Vanilla
        VanishEvents.VANISH_MESSAGE_EVENT.register(serverPlayer -> Component.translatable("multiplayer.player.left", serverPlayer.getDisplayName()).withStyle(ChatFormatting.YELLOW));
        VanishEvents.UN_VANISH_MESSAGE_EVENT.register(serverPlayer -> Component.translatable("multiplayer.player.joined", serverPlayer.getDisplayName()).withStyle(ChatFormatting.YELLOW));
        // Styled Chat
        if (STYLED_CHAT) {
            VanishEvents.UN_VANISH_MESSAGE_EVENT.register(StyledChatStyles::getJoin);
            VanishEvents.VANISH_MESSAGE_EVENT.register(StyledChatStyles::getLeft);
        }

        if (BLUEMAP) {
            BlueMapAPI.onEnable(blueMapAPI -> {
                // BlueMapAPI.onEnable may be called multiple times, but we only want to register our events once
                if (!blueMapEventsRegistered) {
                    VanishEvents.VANISH_EVENT.register((player, vanish) -> {
                        BlueMapAPI.getInstance().ifPresent(api -> api.getWebApp().setPlayerVisibility(player.getUUID(), !vanish));
                    });
                    blueMapEventsRegistered = true;
                }
            });
        }
    }

}


