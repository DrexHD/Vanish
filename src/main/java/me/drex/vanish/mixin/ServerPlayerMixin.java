package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @WrapOperation(
            method = "die",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastToTeam(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/network/chat/Component;)V"
            )
    )
    private void vanish_hideDeathMessage(PlayerList playerList, Player player, Component component, Operation<Void> original) {
        if (VanishAPI.isVanished((ServerPlayer) (Object) this)) {
            VanishAPI.broadcastHiddenMessage((ServerPlayer) (Object) this, component);
        } else {
            original.call(playerList, player, component);
        }
    }

    @WrapOperation(
            method = "die",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastToAllExceptTeam(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/network/chat/Component;)V"
            )
    )
    private void vanish_hideDeathMessage2(PlayerList playerList, Player player, Component component, Operation<Void> original) {
        if (VanishAPI.isVanished((ServerPlayer) (Object) this)) {
            VanishAPI.broadcastHiddenMessage((ServerPlayer) (Object) this, component);
        } else {
            original.call(playerList, player, component);
        }
    }

    @WrapOperation(
            method = "die",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"
            )
    )
    private void vanish_hideDeathMessage3(PlayerList playerList, Component component, ChatType chatType, UUID uuid, Operation<Void> original) {
        if (VanishAPI.isVanished((ServerPlayer) (Object) this)) {
            VanishAPI.broadcastHiddenMessage((ServerPlayer) (Object) this, component);
        } else {
            original.call(playerList, component, chatType, uuid);
        }
    }

}
