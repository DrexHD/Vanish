package me.drex.vanish.mixin.death_message;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DamageSource.class)
public abstract class DamageSourceMixin {
    @Shadow @Final private @Nullable Entity causingEntity;

    @Shadow @Final private @Nullable Entity directEntity;

    @WrapOperation(
        method = "getLocalizedDeathMessage",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/damagesource/DamageSource;causingEntity:Lnet/minecraft/world/entity/Entity;"
        )
    )
    public Entity hideVanished(DamageSource instance, Operation<Entity> original) {
        Entity entity = original.call(instance);
        if (VanishAPI.isVanished(entity)) {
            return null;
        }
        return entity;
    }

    @WrapOperation(
        method = "getLocalizedDeathMessage",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/damagesource/DamageSource;directEntity:Lnet/minecraft/world/entity/Entity;"
        )
    )
    public Entity hideVanished2(DamageSource instance, Operation<Entity> original) {
        Entity entity = original.call(instance);
        if (VanishAPI.isVanished(entity)) {
            return null;
        }
        return entity;
    }

    @WrapOperation(
        method = "getLocalizedDeathMessage",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;getKillCredit()Lnet/minecraft/world/entity/LivingEntity;"
        )
    )
    public LivingEntity hideVanished3(LivingEntity instance, Operation<LivingEntity> original) {
        LivingEntity entity = original.call(instance);
        if (VanishAPI.isVanished(entity)) {
            return null;
        }
        return entity;
    }

    @WrapOperation(
        method = "getLocalizedDeathMessage",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;",
            ordinal = 3
        )
    )
    public MutableComponent fixMessage(String string, Object[] objects, Operation<MutableComponent> original) {
        if (this.causingEntity != null || this.directEntity != null) {
            // A causing or direct entity existed, but was vanished.
            // The death message expects two arguments, but now there is only one, so we need to completely replace it
            return Component.translatable("death.attack.generic", objects[0]);
        }
        return original.call(string, objects);
    }
}
