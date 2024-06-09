package me.drex.vanish.mixin.interaction;

import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(SimpleCriterionTrigger.class)
public abstract class SimpleCriterionTriggerMixin {

    @Inject(method = "trigger", at = @At("HEAD"), cancellable = true)
    public void vanish_preventAdvancementProgress(ServerPlayer player, Predicate<Object> predicate, CallbackInfo ci) {
        if (ConfigManager.vanish().interaction.advancementProgress && VanishAPI.isVanished(player)) {
            ci.cancel();
        }
    }

}
