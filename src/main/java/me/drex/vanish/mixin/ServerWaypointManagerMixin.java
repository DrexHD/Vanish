package me.drex.vanish.mixin;

//? >= 1.21.10 {
import com.google.common.collect.Table;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.waypoints.ServerWaypointManager;
import net.minecraft.world.waypoints.WaypointTransmitter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWaypointManager.class)
public abstract class ServerWaypointManagerMixin {

    @Shadow
    @Final
    private Table<ServerPlayer, WaypointTransmitter, WaypointTransmitter.Connection> connections;

    @Inject(method = "createConnection", at = @At("HEAD"), cancellable = true)
    private void createConnection(ServerPlayer serverPlayer, WaypointTransmitter waypointTransmitter, CallbackInfo ci) {
        if (waypointTransmitter instanceof ServerPlayer target
                && !VanishAPI.canSeePlayer(target, serverPlayer)) {
            ci.cancel();
        }
    }

    @Inject(method = "updateConnection", at = @At("HEAD"), cancellable = true)
    private void updateConnection(ServerPlayer serverPlayer, WaypointTransmitter waypointTransmitter, WaypointTransmitter.Connection connection, CallbackInfo ci) {
        if (waypointTransmitter instanceof ServerPlayer target
                && !VanishAPI.canSeePlayer(target, serverPlayer)) {
            this.connections.remove(serverPlayer, waypointTransmitter);
            connection.disconnect();
            ci.cancel();
        }
    }
}
//?}