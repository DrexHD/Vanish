package me.drex.vanish.mixin;

import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.commands.MsgCommand;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(MsgCommand.class)
public class MsgCommandMixin {
    
    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
    private static void vanish_stopMessage(CommandSourceStack commandSourceStack, Collection<ServerPlayer> collection, PlayerChatMessage playerChatMessage, CallbackInfo ci) {
        ServerPlayer player = commandSourceStack.getPlayer();
        if (player == null) return;

        if (VanishAPI.isVanished(player) && ConfigManager.vanish().disableMsg) {
            player.sendSystemMessage(Component.translatable("text.vanish.chat.disabled").withStyle(ChatFormatting.RED));
            ci.cancel();
        }
    }
}