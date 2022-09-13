package me.drex.vanish.mixin.packets;

import me.drex.vanish.util.VanishedEntityPackets;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientboundMoveEntityPacket.class)
public abstract class ClientboundMoveEntityPacketMixin implements VanishedEntityPackets {
    @Shadow
    @Final
    protected int entityId;

    @Override
    public int getEntityId() {
        return this.entityId;
    }
}
