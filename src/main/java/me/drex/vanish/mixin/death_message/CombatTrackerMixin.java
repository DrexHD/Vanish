package me.drex.vanish.mixin.death_message;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CombatTracker.class)
public abstract class CombatTrackerMixin {
    @WrapOperation(
        method = "getDeathMessage",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/damagesource/DamageSource;getEntity()Lnet/minecraft/world/entity/Entity;"
        )
    )
    public Entity hideVanished(DamageSource instance, Operation<Entity> original) {
        Entity entity = original.call(instance);
        if (VanishAPI.isVanished(entity)) {
            return null;
        }
        return entity;
    }
}
