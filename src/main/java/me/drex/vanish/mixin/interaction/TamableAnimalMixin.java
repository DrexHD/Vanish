package me.drex.vanish.mixin.interaction;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TamableAnimal.class)
public abstract class TamableAnimalMixin {
    @WrapOperation(
        method = "unableToMoveToOwner",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;isSpectator()Z"
        )
    )
    public boolean preventAnimalFollow(LivingEntity entity, Operation<Boolean> original) {
        Boolean isSpectator = original.call(entity);
        if (ConfigManager.vanish().hideFromEntities) {
            return isSpectator || VanishAPI.isVanished(entity);
        }
        return isSpectator;
    }
}
