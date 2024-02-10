package me.drex.vanish.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerCommonPacketListenerImplMixin {

    @Shadow
    @Final
    protected MinecraftServer server;

    @Shadow
    public abstract void send(Packet<?> packet);

    @Inject(
        method = "send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    public void vanish_modifyPackets(Packet<?> packet, PacketSendListener packetSendListener, CallbackInfo ci) {
        if ((Object) this instanceof ServerGamePacketListenerImpl listener) {
            if (packet instanceof ClientboundTakeItemEntityPacket takeItemEntityPacket) {
                Entity entity = listener.player.level().getEntity(takeItemEntityPacket.getPlayerId());
                if (entity instanceof ServerPlayer actor && !VanishAPI.canSeePlayer(actor, listener.player)) {
                    this.send(new ClientboundRemoveEntitiesPacket(takeItemEntityPacket.getItemId()));
                    ci.cancel();
                }
            } else if (packet instanceof ClientboundPlayerInfoUpdatePacket playerInfoPacket) {
                ObjectArrayList<ServerPlayer> modifiedEntries = new ObjectArrayList<>();
                int visible = 0;
                for (ClientboundPlayerInfoUpdatePacket.Entry playerUpdate : playerInfoPacket.entries()) {
                    ServerPlayer player = server.getPlayerList().getPlayer(playerUpdate.profileId());
                    if (VanishAPI.canSeePlayer(player, listener.player)) {
                        visible++;
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
    }


}
