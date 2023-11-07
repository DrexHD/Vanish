package me.drex.vanish.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.drex.vanish.VanishMod;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import me.drex.vanish.util.VanishManager;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.Collections;

public class VanishCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection selection) {
        dispatcher.register(
            Commands.literal("vanish")
                .requires(src -> Permissions.check(src, "vanish.command.vanish", 2))
                .executes(VanishCommand::vanish)
                .then(
                    Commands.literal("reload")
                        .requires(src -> Permissions.check(src, "vanish.command.vanish.reload", 2))
                        .executes(VanishCommand::reload)
                ).then(
                    Commands.literal("on")
                        .executes(ctx -> vanish(ctx.getSource(), true, Collections.singleton(ctx.getSource().getPlayerOrException().getGameProfile())))
                        .then(
                            Commands.argument("players", GameProfileArgument.gameProfile())
                                .requires(src -> Permissions.check(src, "vanish.command.vanish.other", 2))
                                .executes(ctx -> vanish(ctx.getSource(), true, GameProfileArgument.getGameProfiles(ctx, "players")))
                        )
                ).then(
                    Commands.literal("off")
                        .executes(ctx -> vanish(ctx.getSource(), false, Collections.singleton(ctx.getSource().getPlayerOrException().getGameProfile())))
                        .then(
                            Commands.argument("players", GameProfileArgument.gameProfile())
                                .requires(src -> Permissions.check(src, "vanish.command.vanish.other", 2))
                                .executes(ctx -> vanish(ctx.getSource(), false, GameProfileArgument.getGameProfiles(ctx, "players")))
                        )
                )
        );
    }

    public static int vanish(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        boolean vanished = VanishAPI.isVanished(player);
        return vanish(ctx.getSource(), !vanished, Collections.singleton(player.getGameProfile()));
    }

    public static int reload(CommandContext<CommandSourceStack> ctx) {
        try {
            ConfigManager.load();
            ctx.getSource().sendSuccess(() -> Component.translatable("text.vanish.command.vanish.reload"), false);
            return 1;
        } catch (Exception e) {
            ctx.getSource().sendFailure(Component.translatable("text.vanish.command.vanish.reload.error"));
            VanishMod.LOGGER.error("An error occurred while loading the config, keeping old values", e);
            return 0;
        }
    }

    public static int vanish(CommandSourceStack src, boolean vanish, Collection<GameProfile> targets) throws CommandSyntaxException {
        int result = 0;
        for (GameProfile target : targets) {
            if (!VanishManager.setVanished(target, src.getServer(), vanish)) continue;
            ServerPlayer player = src.getPlayer();
            if (player != null && player.getGameProfile().equals(target)) {
                src.sendSuccess(() -> Component.translatable(vanish ? "text.vanish.command.vanish.enable" : "text.vanish.command.vanish.disable"), false);
            } else {
                src.sendSuccess(() -> Component.translatable(vanish ? "text.vanish.command.vanish.enable.other" : "text.vanish.command.vanish.disable.other", target.getName()), false);
            }
            result++;
        }
        return result;
    }

}
