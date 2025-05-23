package me.drex.vanish.mixin.interaction;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.VanishMod;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @WrapOperation(
        method = "isPushable",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;isSpectator()Z"
        )
    )
    public boolean preventPushing(LivingEntity entity, Operation<Boolean> original) {
        return original.call(entity) || (ConfigManager.vanish().interaction.entityCollisions && VanishAPI.isVanished(entity));
    }

    //? if >= 1.21.2 {
    @WrapOperation(
        method = "canEquipWithDispenser",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;isSpectator()Z"
        )
    )
    public boolean preventArmorItemEquip(LivingEntity entity, Operation<Boolean> original) {
        return original.call(entity) || (ConfigManager.vanish().interaction.entityPickup && VanishAPI.isVanished(entity));
    }
    //?}


}
