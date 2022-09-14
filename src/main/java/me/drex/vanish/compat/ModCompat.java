package me.drex.vanish.compat;

import eu.pb4.styledchat.config.ConfigManager;
import me.drex.vanish.api.VanishEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class ModCompat {

    public static final boolean STYLED_CHAT = FabricLoader.getInstance().isModLoaded("styledchat");

    public static void register() {
        // Styled Chat
        if (STYLED_CHAT) {
            VanishEvents.UN_VANISH_MESSAGE_EVENT.register(serverPlayer -> ConfigManager.getConfig().getJoin(serverPlayer));
            VanishEvents.VANISH_MESSAGE_EVENT.register(serverPlayer -> ConfigManager.getConfig().getLeft(serverPlayer));
        }
    }

}
