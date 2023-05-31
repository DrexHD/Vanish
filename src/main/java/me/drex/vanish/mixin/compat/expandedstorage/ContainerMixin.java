package me.drex.vanish.mixin.compat.expandedstorage;

import compasses.expandedstorage.common.block.entity.BarrelBlockEntity;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {BarrelBlockEntity.class}, targets = "compasses/expandedstorage/common/block/entity/extendable/InventoryBlockEntity$1")
public abstract class ContainerMixin {

    @Inject(method = "startOpen", at = @At("HEAD"), cancellable = true)
    public void vanish_cancelOpenAnimation(Player player, CallbackInfo ci) {
        if (player instanceof ServerPlayer serverPlayer && VanishAPI.isVanished(serverPlayer)) {
            ci.cancel();
        }
    }

    @Inject(method = "stopOpen", at = @At("HEAD"), cancellable = true)
    public void vanish_cancelCloseAnimation(Player player, CallbackInfo ci) {
        if (player instanceof ServerPlayer serverPlayer && VanishAPI.isVanished(serverPlayer)) {
            ci.cancel();
        }
    }

}
