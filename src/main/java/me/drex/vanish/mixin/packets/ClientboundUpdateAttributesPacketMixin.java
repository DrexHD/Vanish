package me.drex.vanish.mixin.packets;

import me.drex.vanish.util.VanishedEntityPackets;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientboundUpdateAttributesPacket.class)
public abstract class ClientboundUpdateAttributesPacketMixin implements VanishedEntityPackets {
    @Shadow
    @Final
    private int entityId;

    @Override
    public int getEntityId() {
        return this.entityId;
    }
}
