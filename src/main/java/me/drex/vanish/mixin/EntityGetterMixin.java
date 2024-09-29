package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.EntityGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// Priority is increased for compatibility with
// https://github.com/Tuinity/Moonrise/blob/master/src/main/java/ca/spottedleaf/moonrise/mixin/collisions/EntityGetterMixin.java#L74-L104
@Mixin(value = EntityGetter.class, priority = 1500)
public interface EntityGetterMixin {

    @WrapOperation(
        method = "isUnobstructed",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/Entity;blocksBuilding:Z"
        )
    )
    default boolean vanish_noBlockObstruction(Entity entity, Operation<Boolean> original) {
        if (entity instanceof ServerPlayer serverPlayer && VanishAPI.isVanished(serverPlayer)) {
            return false;
        }
        return original.call(entity);
    }

}
