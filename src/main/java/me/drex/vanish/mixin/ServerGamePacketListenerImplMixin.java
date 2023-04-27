package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {

    @Shadow
    public ServerPlayer player;

    @Shadow
    public abstract void send(Packet<?> packet);

    @Shadow
    @Final
    private MinecraftServer server;

    @Inject(
            method = "send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void vanish_modifyPackets(Packet<?> packet, @Nullable PacketSendListener packetSendListener, CallbackInfo ci) {
        if (packet instanceof ClientboundTakeItemEntityPacket takeItemEntityPacket) {
            Entity entity = this.player.level().getEntity(takeItemEntityPacket.getPlayerId());
            if (entity instanceof ServerPlayer executive && !VanishAPI.canSeePlayer(executive, this.player)) {
                this.send(new ClientboundRemoveEntitiesPacket(takeItemEntityPacket.getItemId()));
                ci.cancel();
            }
        } else if (packet instanceof ClientboundPlayerInfoUpdatePacket playerInfoPacket) {
            ObjectArrayList<ServerPlayer> modifiedEntries = new ObjectArrayList<>();
            int visible = 0;
            for (ClientboundPlayerInfoUpdatePacket.Entry playerUpdate : playerInfoPacket.entries()) {
                if (VanishAPI.canSeePlayer(server, playerUpdate.profileId(), this.player)) {
                    visible++;
                    ServerPlayer player = server.getPlayerList().getPlayer(playerUpdate.profileId());
                    if (player != null) modifiedEntries.add(player);
                }
            }
            if (visible != playerInfoPacket.entries().size()) {
                if (!modifiedEntries.isEmpty()) {
                    this.send(new ClientboundPlayerInfoUpdatePacket(playerInfoPacket.actions(), modifiedEntries));
                }
                ci.cancel();
            }
        }
    }

    @WrapOperation(
            method = "onDisconnect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
            )
    )
    public void vanish_hideLeaveMessage(PlayerList playerList, Component component, boolean bl, Operation<Void> original) {
        if (VanishAPI.isVanished(this.player)) {
            VanishAPI.broadcastHiddenMessage(this.player, component);
        } else {
            original.call(playerList, component, bl);
        }
    }

}
