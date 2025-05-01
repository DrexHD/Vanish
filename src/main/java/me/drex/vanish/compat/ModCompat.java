package me.drex.vanish.compat;

import de.bluecolored.bluemap.api.BlueMapAPI;
import eu.pb4.styledchat.StyledChatStyles;
import me.drex.vanish.VanishMod;
import me.drex.vanish.api.VanishEvents;
import me.lucko.fabric.api.permissions.v0.Options;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.util.TriState;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import xyz.jpenilla.squaremap.api.*;

public class ModCompat {

    public static final boolean STYLED_CHAT = FabricLoader.getInstance().isModLoaded("styledchat");
    public static final boolean BLUEMAP = FabricLoader.getInstance().isModLoaded("bluemap");
    public static final boolean DYNMAP = FabricLoader.getInstance().isModLoaded("dynmap");
    public static final boolean SQUAREMAP = FabricLoader.getInstance().isModLoaded("squaremap");
    public static final boolean PL3XMAP = FabricLoader.getInstance().isModLoaded("pl3xmap");
    public static final ResourceLocation VANISH_ON_JOIN = ResourceLocation.fromNamespaceAndPath(VanishMod.MOD_ID, "vanish_on_joín");
    public static boolean blueMapEventsRegistered = false;

    public static void init() {
        // Vanilla
        VanishEvents.VANISH_MESSAGE_EVENT.register(serverPlayer -> Component.translatable("multiplayer.player.left", serverPlayer.getDisplayName()).withStyle(ChatFormatting.YELLOW));
        VanishEvents.UN_VANISH_MESSAGE_EVENT.register(serverPlayer -> Component.translatable("multiplayer.player.joined", serverPlayer.getDisplayName()).withStyle(ChatFormatting.YELLOW));

        // Util
        // Allow mods to use the default phase to (conditionally) overwrite this feature
        VanishEvents.JOIN_EVENT.addPhaseOrdering(Event.DEFAULT_PHASE, VANISH_ON_JOIN);
        VanishEvents.JOIN_EVENT.register(VANISH_ON_JOIN, player -> Options.get(player, "vanish_on_join", Boolean::valueOf).map(TriState::of).orElse(TriState.DEFAULT));

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
        if (DYNMAP) {
            DynmapCompat.init();
        }
        if (PL3XMAP) {
            Pl3xmapCompat.init();
        }
        if (SQUAREMAP) {
            VanishEvents.VANISH_EVENT.register((player, vanish) -> {
                if (vanish) {
                    SquaremapProvider.get().playerManager().hide(player.getUUID(), true);
                } else {
                    SquaremapProvider.get().playerManager().show(player.getUUID(), true);
                }
            });
        }
    }

}


