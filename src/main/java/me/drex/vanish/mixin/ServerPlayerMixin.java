package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @WrapOperation(
            method = "die",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemToTeam(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/network/chat/Component;)V"
            )
    )
    private void vanish_hideTeamDeathMessage(PlayerList playerList, Player player, Component component, Operation<Void> original) {
        if (VanishAPI.isVanished((ServerPlayer) (Object) this)) {
            // This will send the message to all players, who can view vanished players instead of team only
            // If this causes any issues for you, make sure to open an issue
            VanishAPI.broadcastHiddenMessage((ServerPlayer) (Object) this, component);
        } else {
            original.call(playerList, player, component);
        }
    }

    @WrapOperation(
            method = "die",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemToAllExceptTeam(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/network/chat/Component;)V"
            )
    )
    private void vanish_hideExceptTeamDeathMessage(PlayerList playerList, Player player, Component component, Operation<Void> original) {
        if (VanishAPI.isVanished((ServerPlayer) (Object) this)) {
            // This will send the message to all players, who can view vanished players instead of all except the same team
            // If this causes any issues for you, make sure to open an issue
            VanishAPI.broadcastHiddenMessage((ServerPlayer) (Object) this, component);
        } else {
            original.call(playerList, player, component);
        }
    }

    @WrapOperation(
            method = "die",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
            )
    )
    private void vanish_hideDeathMessage(PlayerList playerList, Component component, boolean overlay, Operation<Void> original) {
        if (VanishAPI.isVanished((ServerPlayer) (Object) this)) {
            VanishAPI.broadcastHiddenMessage((ServerPlayer) (Object) this, component);
        } else {
            original.call(playerList, component, overlay);
        }
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void vanish_stopAttack(Entity entity, CallbackInfo ci) {
        if (VanishAPI.isVanished((ServerPlayer) (Object) this) && ConfigManager.vanish().interaction.worldInteractions) ci.cancel();
    }

    @Inject(method = "mayInteract", at = @At("HEAD"), cancellable = true)
    private void vanish_stopInteract(Level level, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (VanishAPI.isVanished((ServerPlayer) (Object) this) && ConfigManager.vanish().interaction.worldInteractions) cir.setReturnValue(false);
    }

    @Inject(method = "startRiding", at = @At("HEAD"), cancellable = true)
    private void vanish_stopRide(Entity entity, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        if (VanishAPI.isVanished((ServerPlayer) (Object) this) && ConfigManager.vanish().interaction.worldInteractions) cir.setReturnValue(false);
    }
}
