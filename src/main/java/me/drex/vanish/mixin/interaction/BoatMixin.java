package me.drex.vanish.mixin.interaction;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Boat.class)
public class BoatMixin {

    @ModifyReturnValue(method = "canVehicleCollide", at = @At("RETURN"))
    private static boolean vanish_preventCollision(boolean original, Entity vehicle, Entity other) {
        if (ConfigManager.vanish().interaction.entityCollisions && VanishAPI.isVanished(other)) {
            return false;
        }
        return original;
    }

}