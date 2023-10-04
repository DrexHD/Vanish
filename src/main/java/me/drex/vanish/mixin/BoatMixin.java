package me.drex.vanish.mixin;

import me.drex.vanish.api.VanishAPI;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Boat.class)
public class BoatMixin {

    @Inject(method = "canVehicleCollide", at = @At("HEAD"), cancellable = true)
    private static void stopCollision(Entity entity1, Entity entity2, CallbackInfoReturnable<Boolean> cir) {
        if (entity2 instanceof ServerPlayer player) {
            if (VanishAPI.isVanished(player)) cir.setReturnValue(false);
        }
    }
}