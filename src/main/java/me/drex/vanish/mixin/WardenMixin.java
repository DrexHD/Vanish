package me.drex.vanish.mixin;

import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.warden.Warden;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Warden.class)
public abstract class WardenMixin {

    @Inject(method = "canTargetEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/warden/Warden;isAlliedTo(Lnet/minecraft/world/entity/Entity;)Z"), cancellable = true)
    public void vanish_excludeVanished(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (ConfigManager.vanish().hideFromEntities && VanishAPI.isVanished(entity)) {
            cir.setReturnValue(false);
        }
    }

}
