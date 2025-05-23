package me.drex.vanish.mixin.interaction;

import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {FarmBlock.class, TurtleEggBlock.class})
public abstract class FallOnBlockMixin {

    @Inject(method = "fallOn", at = @At("HEAD"), cancellable = true)
    private void cancelEntityFallOnBlock(Level level, BlockState blockState, BlockPos blockPos, Entity entity, /*? if >= 1.21.5 {*/ double /*?} else {*/ /*float *//*?}*/ d, CallbackInfo ci) {
        if (VanishAPI.isVanished(entity) && ConfigManager.vanish().interaction.blocks) ci.cancel();
    }

}
