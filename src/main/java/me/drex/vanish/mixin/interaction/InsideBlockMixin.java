package me.drex.vanish.mixin.interaction;

import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.BigDripleafBlock;
import net.minecraft.world.level.block.TripWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {BasePressurePlateBlock.class, BigDripleafBlock.class, TripWireBlock.class})
public abstract class InsideBlockMixin {

    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
    private void vanish_cancelEntityInsideBlock(
        BlockState blockState, Level level, BlockPos blockPos, Entity entity,
        InsideBlockEffectApplier insideBlockEffectApplier, CallbackInfo ci
    ) {
        if (VanishAPI.isVanished(entity) && ConfigManager.vanish().interaction.blocks) ci.cancel();
    }

}
