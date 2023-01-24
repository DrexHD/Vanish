package me.drex.vanish.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.drex.vanish.VanishMod;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
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
                                        .executes(ctx -> vanish(ctx.getSource(), true, Collections.singleton(ctx.getSource().getPlayerOrException())))
                                        .then(
                                                Commands.argument("players", EntityArgument.players())
                                                        .requires(src -> Permissions.check(src, "vanish.command.vanish.other", 2))
                                                        .executes(ctx -> vanish(ctx.getSource(), true, EntityArgument.getPlayers(ctx, "players")))
                                        )
                        ).then(
                                Commands.literal("off")
                                        .executes(ctx -> vanish(ctx.getSource(), false, Collections.singleton(ctx.getSource().getPlayerOrException())))
                                        .then(
                                                Commands.argument("players", EntityArgument.players())
                                                        .requires(src -> Permissions.check(src, "vanish.command.vanish.other", 2))
                                                        .executes(ctx -> vanish(ctx.getSource(), false, EntityArgument.getPlayers(ctx, "players")))
                                        )
                        )
        );
    }

    public static int vanish(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        boolean vanished = VanishAPI.isVanished(player);
        return vanish(ctx.getSource(), !vanished, Collections.singleton(player));
    }

    public static int reload(CommandContext<CommandSourceStack> ctx) {
        try {
            ConfigManager.load();
            ctx.getSource().sendSuccess(Component.translatable("text.vanish.command.vanish.reload"), false);
            return 1;
        } catch (Exception e) {
            ctx.getSource().sendFailure(Component.translatable("text.vanish.command.vanish.reload.error"));
            VanishMod.LOGGER.error("An error occurred while loading the config, keeping old values", e);
            return 0;
        }
    }

    public static int vanish(CommandSourceStack src, boolean vanish, Collection<ServerPlayer> targets) throws CommandSyntaxException {
        int result = 0;
        for (ServerPlayer target : targets) {
            if (!VanishAPI.setVanish(target, vanish)) continue;
            if (src.getPlayerOrException() == target) {
                src.sendSuccess(Component.translatable(vanish ? "text.vanish.command.vanish.enable" : "text.vanish.command.vanish.disable"), false);
            } else {
                src.sendSuccess(Component.translatable(vanish ? "text.vanish.command.vanish.enable.other" : "text.vanish.command.vanish.disable.other", target.getDisplayName()), false);
                target.sendSystemMessage(Component.translatable(vanish ? "text.vanish.command.vanish.enable" : "text.vanish.command.vanish.disable"));
            }
            result++;
        }
        return result;
    }

}
