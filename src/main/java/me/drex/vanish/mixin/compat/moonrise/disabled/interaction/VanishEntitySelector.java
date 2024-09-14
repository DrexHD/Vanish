package me.drex.vanish.mixin.compat.moonrise.disabled.interaction;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.VanishMod;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.EntityGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

public class VanishEntitySelector {
    @Mixin(EntityGetter.class)
    public interface EntityGetterMixin {
        @WrapOperation(
            method = "getEntityCollisions",
            at = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntitySelector;NO_SPECTATORS:Ljava/util/function/Predicate;"
            )
        )
        default Predicate<Entity> vanish_preventEntityCollisions(Operation<Predicate<Entity>> original) {
            return ConfigManager.vanish().interaction.entityCollisions ? VanishMod.NO_SPECTATORS_AND_NO_VANISH : original.call();
        }

        @WrapOperation(
            method = "getEntityCollisions",
            at = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntitySelector;CAN_BE_COLLIDED_WITH:Ljava/util/function/Predicate;"
            )
        )
        default Predicate<Entity> vanish_preventEntityCollisions2(Operation<Predicate<Entity>> original) {
            return ConfigManager.vanish().interaction.entityCollisions ? VanishMod.CAN_BE_COLLIDED_WITH_AND_NO_VANISH : original.call();
        }
    }
}
