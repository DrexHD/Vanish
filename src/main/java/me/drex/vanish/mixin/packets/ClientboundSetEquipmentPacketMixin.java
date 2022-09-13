package me.drex.vanish.mixin.packets;

import me.drex.vanish.util.VanishedEntityPackets;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientboundSetEquipmentPacket.class)
public abstract class ClientboundSetEquipmentPacketMixin implements VanishedEntityPackets {
    @Shadow
    @Final
    private int entity;

    @Override
    public int getEntityId() {
        return this.entity;
    }
}
