package me.drex.vanish.util;

import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import eu.pb4.placeholders.api.parsers.NodeParser;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static me.drex.vanish.VanishMod.MOD_ID;

public class VanishPlaceHolders {

    public static final ResourceLocation VANISHED = ResourceLocation.fromNamespaceAndPath(MOD_ID, "vanished");
    public static final ResourceLocation ONLINE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "online");

    private static final NodeParser PARSER = NodeParser.builder()
        .simplifiedTextFormat()
        .quickText()
        .build();

    public static void register() {
        Placeholders.register(VANISHED, (context, argument) -> {
            if (context.player() != null) {
                return VanishAPI.isVanished(context.player()) ? PlaceholderResult.value(PARSER.parseText(ConfigManager.vanish().placeHolderDisplay, context.asParserContext())) : PlaceholderResult.value(Component.empty());
            }
            return PlaceholderResult.invalid("No player!");
        });
        Placeholders.register(ONLINE, (context, argument) -> {
            if (context.hasEntity()) {
                return PlaceholderResult.value(String.valueOf(VanishAPI.getVisiblePlayers(context.source()).size()));
            } else {
                return PlaceholderResult.value(String.valueOf(VanishAPI.getVisiblePlayers(context.source().withPermission(0)).size()));
            }
        });

    }

}
