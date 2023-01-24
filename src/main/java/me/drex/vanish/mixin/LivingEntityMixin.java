package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @WrapOperation(
            method = "checkFallDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"
            )
    )
    public <T extends ParticleOptions> int vanish_hideFallingParticles(ServerLevel level, T particleOptions, double x, double y, double z, int count, double dx, double dy, double dz, double speed, Operation<Integer> original) {
        if (!VanishAPI.isVanished((LivingEntity) (Object) this)) {
            return original.call(level, particleOptions, x, y, z, count, dx, dy, dz, speed);
        }
        return 0;
    }

    @Inject(method = "canBeSeenByAnyone", at = @At("HEAD"), cancellable = true)
    public void vanish_hideFromEntities(CallbackInfoReturnable<Boolean> cir) {
        if (ConfigManager.vanish().hideFromEntities && VanishAPI.isVanished((LivingEntity) (Object) this)) {
            cir.setReturnValue(false);
        }
    }

}
