package me.drex.vanish.mixin.interaction;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.trialspawner.PlayerDetector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerDetector.class)
public interface PlayerDetectorMixin {
    // NO_CREATIVE_PLAYERS
    @WrapOperation(method = "method_56723", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z"))
    private static boolean vanish_preventTrialSpawning(Player player, Operation<Boolean> original) {
        boolean cancel = ConfigManager.vanish().interaction.mobSpawning && VanishAPI.isVanished(player);
        return original.call(player) || cancel;
    }

    // INCLUDING_CREATIVE_PLAYERS
    @WrapOperation(method = "method_56721", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z"))
    private static boolean vanish_preventVaultOpening(Player player, Operation<Boolean> original) {
        boolean cancel = ConfigManager.vanish().interaction.blocks && VanishAPI.isVanished(player);
        return original.call(player) || cancel;
    }
}
