package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin {

    @ModifyReturnValue(
            method = "skipPlayer",
            at = @At("RETURN")
    )
    public boolean vanish_preventChunkGeneration(boolean original, ServerPlayer player) {
        return original || VanishAPI.isVanished(player);
    }

    @WrapOperation(
            method = "playerIsCloseEnoughForSpawning",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;isSpectator()Z"
            )
    )
    public boolean vanish_preventMobSpawning(ServerPlayer player, Operation<Boolean> original) {
        return original.call(player) || VanishAPI.isVanished(player);
    }

}
