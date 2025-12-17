package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import me.drex.vanish.util.Arguments;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientboundPlayerInfoUpdatePacket.Entry.class)
public abstract class ClientboundPlayerInfoUpdatePacket$EntryMixin {
    @WrapOperation(
        method = "<init>(Lnet/minecraft/server/level/ServerPlayer;)V",
        at = @At(
            value = "INVOKE",
            //? if >= 1.21.5 {
            target = "Lnet/minecraft/server/level/ServerPlayer;gameMode()Lnet/minecraft/world/level/GameType;"
            //?} else {
            /*target = "Lnet/minecraft/server/level/ServerPlayerGameMode;getGameModeForPlayer()Lnet/minecraft/world/level/GameType;"
            *///?}
        )
    )
    private static GameType hideGameMode(/*? if >= 1.21.5 {*/ ServerPlayer /*?} else {*/ /*ServerPlayerGameMode *//*?}*/ instance, Operation<GameType> original, @Local(argsOnly = true) ServerPlayer actor) {
        if (ConfigManager.vanish().hideGameMode) {
            ServerPlayer observer = Arguments.PACKET_CONTEXT.get();
            if (observer != null) {
                if (!VanishAPI.canViewVanished(observer) && actor != observer) {
                    return GameType.DEFAULT_MODE;
                }
            }
        }
        return original.call(instance);
    }
}
