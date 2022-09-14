package me.drex.vanish.util;

import eu.pb4.placeholders.PlaceholderAPI;
import eu.pb4.placeholders.PlaceholderResult;
import eu.pb4.placeholders.TextParser;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.resources.ResourceLocation;

import static me.drex.vanish.VanishMod.MOD_ID;

public class VanishPlaceHolders {

    public static final ResourceLocation VANISHED = new ResourceLocation(MOD_ID, "vanished");
    public static final ResourceLocation ONLINE = new ResourceLocation(MOD_ID, "online");

    public static void register() {
        PlaceholderAPI.register(VANISHED, context -> {
            if (context.getPlayer() != null) {
                return VanishAPI.isVanished(context.getPlayer()) ? PlaceholderResult.value(TextParser.parse(ConfigManager.INSTANCE.vanish().placeHolderDisplay)) : PlaceholderResult.value("");
            }
            return PlaceholderResult.invalid("No player!");
        });
        PlaceholderAPI.register(ONLINE, context -> {
            if (context.getPlayer() != null) {
                return PlaceholderResult.value(String.valueOf(VanishAPI.getVisiblePlayers(context.getPlayer().createCommandSourceStack()).size()));
            } else {
                return PlaceholderResult.value(String.valueOf(VanishAPI.getVisiblePlayers(context.getServer().createCommandSourceStack().withPermission(0)).size()));
            }
        });

    }

}
