package me.drex.vanish.util;

import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import eu.pb4.placeholders.api.parsers.NodeParser;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
//? if > 1.21.10 {
import net.minecraft.server.permissions.PermissionSet;
//? }

import static me.drex.vanish.VanishMod.MOD_ID;

public class VanishPlaceHolders {

    public static final Identifier VANISHED = Identifier.fromNamespaceAndPath(MOD_ID, "vanished");
    public static final Identifier ONLINE = Identifier.fromNamespaceAndPath(MOD_ID, "online");

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
                return PlaceholderResult.value(String.valueOf(VanishAPI.getVisiblePlayers(context.source().withPermission(/*? if > 1.21.10 {*/ PermissionSet.NO_PERMISSIONS /*?} else {*//*0*//*?}*/)).size()));
            }
        });

    }

}
