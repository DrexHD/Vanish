package me.drex.vanish.mixin;

//? >= 1.21.6 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.waypoints.WaypointTransmitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WaypointTransmitter.class)
public interface WaypointTransmitterMixin {
    @WrapOperation(
        method = "doesSourceIgnoreReceiver",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;isSpectator()Z"
        )
    )
    private static boolean hideWaypoints(LivingEntity entity, Operation<Boolean> original) {
        return original.call(entity) || VanishAPI.isVanished(entity);
    }
}
//?} else {

/*import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MinecraftServer.class)
public abstract class WaypointTransmitterMixin {
}
*///?}