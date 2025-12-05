package me.drex.vanish.mixin.interaction;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Predicate;

@Mixin(SimpleCriterionTrigger.class)
public abstract class SimpleCriterionTriggerMixin<T extends SimpleCriterionTrigger.SimpleInstance> {
    @WrapMethod(method = "trigger")
    public void preventAdvancementProgress(ServerPlayer player, Predicate<T> predicate, Operation<Void> original) {
        if (ConfigManager.vanish().interaction.advancementProgress && VanishAPI.isVanished(player)) {
            return;
        }
        if (ConfigManager.vanish().interaction.spectatorAdvancementProgress && player.isSpectator()) {
            return;
        }
        original.call(player, predicate);
    }
}
