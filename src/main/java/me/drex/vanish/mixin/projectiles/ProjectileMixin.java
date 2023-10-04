package me.drex.vanish.mixin.projectiles;

import me.drex.vanish.api.VanishAPI;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public class ProjectileMixin {

    @Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
    private void cancelCollision(HitResult hitResult, CallbackInfo ci) {
        if (hitResult instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof ServerPlayer player) {
            if (VanishAPI.isVanished(player)) ci.cancel();
        }
    }
}