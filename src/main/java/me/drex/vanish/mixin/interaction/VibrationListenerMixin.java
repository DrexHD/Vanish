package me.drex.vanish.mixin.interaction;

import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VibrationListener.class)
public class VibrationListenerMixin {

    @Inject(method = "scheduleVibration", at = @At("HEAD"), cancellable = true)
    private void vanish_preventEntityVibrations(ServerLevel serverLevel, GameEvent gameEvent, GameEvent.Context context, Vec3 vec3, Vec3 vec32, CallbackInfo ci) {
        Entity sourceEntity = context.sourceEntity();
        if (sourceEntity != null && VanishAPI.isVanished(sourceEntity) && ConfigManager.vanish().interaction.vibrations) ci.cancel();
    }

}
