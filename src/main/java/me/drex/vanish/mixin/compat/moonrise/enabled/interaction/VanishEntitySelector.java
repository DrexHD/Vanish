package me.drex.vanish.mixin.compat.moonrise.enabled.interaction;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.EntityGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

public class VanishEntitySelector {
    @Mixin(value = EntityGetter.class, priority = 1500)
    public interface EntityGetterMixin {
        @WrapOperation(
            method = "getEntityCollisions",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/entity/Entity;isSpectator()Z"
            )
        )
        default boolean vanish_preventEntityCollisions(Entity entity, Operation<Boolean> original) {
            return original.call(entity) || (ConfigManager.vanish().interaction.entityCollisions && VanishAPI.isVanished(entity));
        }

        @WrapOperation(
            method = "getEntityCollisions",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/entity/Entity;canBeCollidedWith()Z"
            )
        )
        default boolean vanish_preventEntityCollisions2(Entity entity, Operation<Boolean> original) {
            return original.call(entity) || (ConfigManager.vanish().interaction.entityCollisions && VanishAPI.isVanished(entity));
        }
    }
}
