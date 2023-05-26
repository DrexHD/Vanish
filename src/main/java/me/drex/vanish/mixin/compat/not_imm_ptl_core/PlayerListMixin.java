package me.drex.vanish.mixin.compat.not_imm_ptl_core;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

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
