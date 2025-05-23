package me.drex.vanish.mixin.interaction;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
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
    public boolean preventChunkGeneration(boolean original, ServerPlayer player) {
        return original || (ConfigManager.vanish().interaction.chunkLoading && VanishAPI.isVanished(player));
    }

    @WrapOperation(
        method = "playerIsCloseEnoughForSpawning",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;isSpectator()Z"
        )
    )
    public boolean preventMobSpawning(ServerPlayer player, Operation<Boolean> original) {
        return original.call(player) || (ConfigManager.vanish().interaction.mobSpawning && VanishAPI.isVanished(player));
    }

}
