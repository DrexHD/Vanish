package me.drex.vanish.mixin;

import me.drex.vanish.api.VanishAPI;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TraceableEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "broadcastToPlayer", at = @At("HEAD"), cancellable = true)
    public void vanish_shouldBroadcast(ServerPlayer other, CallbackInfoReturnable<Boolean> cir) {
        Entity self = (Entity) (Object) this;
        ServerPlayer executive;
        if (self instanceof ServerPlayer player) {
            executive = player;
        } else if (self instanceof TraceableEntity traceableEntity && traceableEntity.getOwner() instanceof ServerPlayer owner) {
            executive = owner;
        } else {
            return;
        }
        if (!VanishAPI.canSeePlayer(executive, other)) {
            cir.setReturnValue(false);
        }
    }

}
