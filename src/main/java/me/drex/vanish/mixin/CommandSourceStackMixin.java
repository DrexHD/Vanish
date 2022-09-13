package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CommandSourceStack.class)
public abstract class CommandSourceStackMixin {

    @Shadow
    @Final
    private @Nullable Entity entity;

    @WrapOperation(
            method = "broadcastToAdmins",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;sendSystemMessage(Lnet/minecraft/network/chat/Component;)V"
            )
    )
    public void vanish_hideCommandFeedback(ServerPlayer receiver, Component component, Operation<Void> original) {
        if (this.entity instanceof ServerPlayer serverPlayer && VanishAPI.isVanished(serverPlayer)) {
            if (VanishAPI.canSeePlayer(serverPlayer, receiver)) {
                MutableComponent note = Component.translatable("text.vanish.chat.hidden").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
                component.getSiblings().add(note);
                original.call(receiver, component);
            }
        } else {
            original.call(receiver, component);
        }
    }

}
