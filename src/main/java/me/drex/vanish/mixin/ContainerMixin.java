package me.drex.vanish.mixin;

import me.drex.vanish.api.VanishAPI;
import net.minecraft.server.level.ServerPlayer;
//? if >= 1.21.9-rc1 {
import net.minecraft.world.entity.ContainerUser;
//? } else {
//import net.minecraft.world.entity.player.Player;
//? }
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {BarrelBlockEntity.class, ChestBlockEntity.class, PlayerEnderChestContainer.class, ShulkerBoxBlockEntity.class})
public abstract class ContainerMixin {

    @Inject(method = "startOpen", at = @At("HEAD"), cancellable = true)
    public void cancelOpenAnimation(/*? if >= 1.21.9-rc1 {*/ ContainerUser /*? } else {*/ /*Player*/ /*?}*/ player, CallbackInfo ci) {
        if (player instanceof ServerPlayer serverPlayer && VanishAPI.isVanished(serverPlayer)) {
            ci.cancel();
        }
    }

    @Inject(method = "stopOpen", at = @At("HEAD"), cancellable = true)
    public void cancelCloseAnimation(/*? if >= 1.21.9-rc1 {*/ ContainerUser /*? } else {*/ /*Player*/ /*?}*/ player, CallbackInfo ci) {
        if (player instanceof ServerPlayer serverPlayer && VanishAPI.isVanished(serverPlayer)) {
            ci.cancel();
        }
    }

}
