package me.drex.vanish.mixin;

//? >= 1.21.6 {

import me.drex.vanish.api.VanishAPI;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.waypoints.WaypointTransmitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaypointTransmitter.class)
public interface WaypointTransmitterMixin {
    @Inject(method = "doesSourceIgnoreReceiver", at = @At("HEAD"), cancellable = true)
    private static void hideWaypoints(LivingEntity livingEntity, ServerPlayer serverPlayer, CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity instanceof ServerPlayer actor && !VanishAPI.canSeePlayer(actor, serverPlayer)) {
            cir.setReturnValue(true);
        }
    }
}
//?} else {

/*import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MinecraftServer.class)
public abstract class WaypointTransmitterMixin {
}
*///?}