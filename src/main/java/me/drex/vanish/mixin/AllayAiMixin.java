package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.allay.AllayAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AllayAi.class)
public abstract class AllayAiMixin {
    @WrapOperation(
        method = "getLikedPlayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;closerThan(Lnet/minecraft/world/entity/Entity;D)Z"
        )
    )
    private static boolean excludeVanished(ServerPlayer instance, Entity entity, double distance, Operation<Boolean> original) {
        return original.call(instance, entity, distance) && !VanishAPI.isVanished(instance);
    }
}
