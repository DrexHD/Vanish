package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.VanishMod;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {

    @Shadow
    public ServerPlayer player;

    @WrapOperation(
        method = "removePlayerFromWorld",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
        )
    )
    public void hideLeaveMessage(PlayerList playerList, Component component, boolean bl, Operation<Void> original) {
        if (VanishAPI.isVanished(this.player)) {
            VanishAPI.broadcastHiddenMessage(this.player, component);
        } else {
            original.call(playerList, component, bl);
        }
    }

    @WrapOperation(
        method = "handleInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/game/ServerboundInteractPacket;getTarget(Lnet/minecraft/server/level/ServerLevel;)Lnet/minecraft/world/entity/Entity;"
        )
    )
    public Entity preventInteraction(ServerboundInteractPacket instance, ServerLevel serverLevel, Operation<Entity> original) {
        Entity entity = original.call(instance, serverLevel);
        if (entity instanceof ServerPlayer actor && !VanishAPI.canSeePlayer(actor, this.player)) {
            return null;
        }
        return entity;
    }

    @Inject(
        method = "handlePlayerAction",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket;getPos()Lnet/minecraft/core/BlockPos;"
        )
    )
    public void beforeHandlePlayerAction(ServerboundPlayerActionPacket serverboundPlayerActionPacket, CallbackInfo ci) {
        VanishMod.ACTIVE_ENTITY.set(this.player);
    }

    @Inject(
        method = "handleUseItemOn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;ackBlockChangesUpTo(I)V"
        )
    )
    public void beforeHandleUseItemOn(ServerboundUseItemOnPacket serverboundUseItemOnPacket, CallbackInfo ci) {
        VanishMod.ACTIVE_ENTITY.set(this.player);
    }

    @Inject(
        method = "handleUseItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;ackBlockChangesUpTo(I)V"
        )
    )
    public void beforeHandleUseItem(ServerboundUseItemPacket serverboundUseItemPacket, CallbackInfo ci) {
        VanishMod.ACTIVE_ENTITY.set(this.player);
    }

    @Inject(
        method = "handleInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;resetLastActionTime()V"
        )
    )
    public void beforeHandleInteract(ServerboundInteractPacket serverboundInteractPacket, CallbackInfo ci) {
        VanishMod.ACTIVE_ENTITY.set(this.player);
    }

    @Inject(
        method = {"handlePlayerAction", "handleUseItemOn", "handleUseItem", "handleInteract"},
        at = @At("RETURN")
    )
    public void afterPacket(CallbackInfo ci) {
        VanishMod.ACTIVE_ENTITY.remove();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void beforeTick(CallbackInfo ci) {
        VanishMod.ACTIVE_ENTITY.set(this.player);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void afterTick(CallbackInfo ci) {
        VanishMod.ACTIVE_ENTITY.remove();
    }

}
