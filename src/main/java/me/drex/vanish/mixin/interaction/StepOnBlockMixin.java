package me.drex.vanish.mixin.interaction;

import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {RedStoneOreBlock.class, SculkSensorBlock.class, SculkShriekerBlock.class, TurtleEggBlock.class})
public abstract class StepOnBlockMixin {

    @Inject(method = "stepOn", at = @At("HEAD"), cancellable = true)
    private void cancelEntityStepOnBlock(Level level, BlockPos blockPos, BlockState blockState, Entity entity, CallbackInfo ci) {
        if (VanishAPI.isVanished(entity) && ConfigManager.vanish().interaction.blocks) ci.cancel();
    }

}
