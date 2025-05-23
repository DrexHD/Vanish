package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MapItemSavedData.class)
public abstract class MapItemSavedDataMixin {

    @Shadow
    protected abstract void removeDecoration(String string);

    @WrapOperation(
        method = "tickCarriedBy",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;trackingPosition:Z",
            ordinal = 0
        )
    )
    private boolean hideFromMap(MapItemSavedData instance, Operation<Boolean> original, @Local MapItemSavedData.HoldingPlayer holdingPlayer) {
        if (VanishAPI.isVanished(holdingPlayer.player)) {
            removeDecoration(holdingPlayer.player.getName().getString());
            return false;
        } else {
            return original.call(instance);
        }
    }

}
