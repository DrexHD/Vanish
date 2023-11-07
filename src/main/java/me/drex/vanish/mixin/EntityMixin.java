package me.drex.vanish.mixin;

import me.drex.vanish.api.VanishAPI;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "broadcastToPlayer", at = @At("HEAD"), cancellable = true)
    public void vanish_shouldBroadcast(ServerPlayer observer, CallbackInfoReturnable<Boolean> cir) {
        Entity self = (Entity) (Object) this;
        ServerPlayer actor;
        if (self instanceof ServerPlayer player) {
            actor = player;
        } else if (self instanceof TraceableEntity traceableEntity && traceableEntity.getOwner() instanceof ServerPlayer owner) {
            actor = owner;
        } else {
            return;
        }
        if (!VanishAPI.canSeePlayer(actor, observer)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
    private void vanish_markRenderInvisible(Player player, CallbackInfoReturnable<Boolean> cir) {
        Entity self = (Entity) (Object) this;
        if (self instanceof ServerPlayer actor && player instanceof ServerPlayer observer && !VanishAPI.canSeePlayer(actor, observer)) {
            cir.setReturnValue(true);
        }
    }

}
