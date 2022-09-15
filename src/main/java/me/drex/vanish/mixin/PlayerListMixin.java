package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @WrapOperation(
            method = "placeNewPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
            )
    )
    public void vanish_hideJoinMessage(PlayerList playerList, Component component, boolean bl, Operation<Void> original, Connection connection, ServerPlayer player) {
        if (VanishAPI.isVanished(player)) {
            VanishAPI.broadcastHiddenMessage(player, component);
        } else {
            original.call(playerList, component, bl);
        }
    }

    @WrapWithCondition(
            method = "broadcast",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"
            )
    )
    public boolean vanish_hideGameEvents(ServerGamePacketListenerImpl packetListener, Packet<?> packet, Player player) {
        if (player instanceof ServerPlayer executor) {
            return VanishAPI.canSeePlayer(executor, packetListener.player);
        } else {
            return true;
        }
    }

}
