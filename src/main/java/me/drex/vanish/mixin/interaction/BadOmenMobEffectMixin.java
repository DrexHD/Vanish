package me.drex.vanish.mixin.interaction;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net/minecraft/world/effect/BadOmenMobEffect")
public abstract class BadOmenMobEffectMixin {

    @WrapOperation(
        method = "applyEffectTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;isSpectator()Z"
        )
    )
    public boolean vanish_preventRaid(LivingEntity livingEntity, Operation<Boolean> original) {
        Boolean isSpectator = original.call(livingEntity);
        if (ConfigManager.vanish().interaction.mobSpawning) {
            return isSpectator || VanishAPI.isVanished(livingEntity);
        }
        return isSpectator;
    }

}
