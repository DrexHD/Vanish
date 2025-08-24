package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import me.drex.vanish.util.Arguments;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientboundPlayerInfoUpdatePacket.Entry.class)
public abstract class ClientboundPlayerInfoUpdatePacket$EntryMixin {
    @WrapOperation(
        method = "<init>(Lnet/minecraft/server/level/ServerPlayer;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;gameMode()Lnet/minecraft/world/level/GameType;"
        )
    )
    private static GameType hideGameMode(ServerPlayer instance, Operation<GameType> original) {
        if (ConfigManager.vanish().hideGameMode) {
            ServerPlayer observer = Arguments.PACKET_CONTEXT.get();
            if (observer != null) {
                if (!VanishAPI.canViewVanished(observer.createCommandSourceStack())) {
                    return GameType.DEFAULT_MODE;
                }
            }
        }
        return original.call(instance);
    }
}
