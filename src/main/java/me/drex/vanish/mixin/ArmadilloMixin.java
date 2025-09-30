package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Armadillo.class)
public abstract class ArmadilloMixin {
    @WrapOperation(
        method = "isScaredBy",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z"
        )
    )
    public boolean preventPhantoms(Player player, Operation<Boolean> original) {
        Boolean isSpectator = original.call(player);
        if (ConfigManager.vanish().hideFromEntities) {
            return isSpectator || VanishAPI.isVanished(player);
        }
        return isSpectator;
    }
}
