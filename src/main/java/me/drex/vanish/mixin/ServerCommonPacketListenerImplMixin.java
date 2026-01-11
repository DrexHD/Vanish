package me.drex.vanish.mixin;

import io.netty.channel.ChannelFutureListener;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import me.drex.vanish.util.Arguments;
//? if < 1.21.6 {
//import net.minecraft.network.PacketSendListener;
//? }
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class ServerCommonPacketListenerImplMixin {

    @Shadow
    @Final
    protected MinecraftServer server;

    @Shadow
    public abstract void send(Packet<?> packet);

    @Inject(
        //? if >= 1.21.6 {
        method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;)V",
        //?} else {
        /*method = "send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V",
        *///?}
        at = @At("HEAD"),
        cancellable = true
    )
    public void modifyPackets(Packet<?> packet, /*? if >= 1.21.6 {*/ ChannelFutureListener /*?} else {*/ /*PacketSendListener *//*?}*/ channelFutureListener, CallbackInfo ci) {
        if ((Object) this instanceof ServerGamePacketListenerImpl listener) {
            if (packet instanceof ClientboundTakeItemEntityPacket takeItemEntityPacket) {
                Entity entity = listener.player.level().getEntity(takeItemEntityPacket.getPlayerId());
                if (entity instanceof ServerPlayer actor && !VanishAPI.canSeePlayer(actor, listener.player)) {
                    this.send(new ClientboundRemoveEntitiesPacket(takeItemEntityPacket.getItemId()));
                    ci.cancel();
                }
            } else if (packet instanceof ClientboundPlayerInfoUpdatePacket playerInfoPacket) {
                // If the packet context is set, this packet was already modified
                if (Arguments.PACKET_CONTEXT.get() != null) return;

                boolean hideGameMode = ConfigManager.vanish().hideGameMode;
                boolean canViewVanished = hideGameMode && VanishAPI.canViewVanished(listener.player);
                ObjectArrayList<ClientboundPlayerInfoUpdatePacket.Entry> modifiedEntries = new ObjectArrayList<>(playerInfoPacket.entries().size());
                boolean changed = false;
                for (ClientboundPlayerInfoUpdatePacket.Entry playerUpdate : playerInfoPacket.entries()) {
                    ServerPlayer player = server.getPlayerList().getPlayer(playerUpdate.profileId());
                    if (player != null && !VanishAPI.canSeePlayer(player, listener.player)) {
                        changed = true;
                        continue;
                    }
                    ClientboundPlayerInfoUpdatePacket.Entry adjustedEntry = playerUpdate;
                    if (hideGameMode && !canViewVanished && player != null && player != listener.player && playerUpdate.gameMode() != GameType.DEFAULT_MODE) {
                        adjustedEntry = new ClientboundPlayerInfoUpdatePacket.Entry(
                            playerUpdate.profileId(),
                            playerUpdate.profile(),
                            playerUpdate.listed(),
                            playerUpdate.latency(),
                            GameType.DEFAULT_MODE,
                            playerUpdate.displayName(),
                            //? if >= 1.21.2 {
                            playerUpdate.showHat(),
                            playerUpdate.listOrder(),
                            //? }
                            playerUpdate.chatSession()
                        );
                        changed = true;
                    }
                    modifiedEntries.add(adjustedEntry);
                }
                if (!changed) return;
                if (!modifiedEntries.isEmpty()) {
                    var prev = Arguments.PACKET_CONTEXT.get();
                    try {
                        Arguments.PACKET_CONTEXT.set(listener.player);
                        ClientboundPlayerInfoUpdatePacket modifiedPacket = new ClientboundPlayerInfoUpdatePacket(playerInfoPacket.actions(), java.util.List.of());
                        ((ClientboundPlayerInfoUpdatePacketAccessor) modifiedPacket).setEntries(modifiedEntries);
                        this.send(modifiedPacket);
                    } finally {
                        Arguments.PACKET_CONTEXT.set(prev);
                    }
                }
                ci.cancel();
            }
        }
    }


}
