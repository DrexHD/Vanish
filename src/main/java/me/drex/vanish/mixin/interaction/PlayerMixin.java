package me.drex.vanish.mixin.interaction;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @WrapWithCondition(method = "touch", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;playerTouch(Lnet/minecraft/world/entity/player/Player;)V"))
    private boolean vanish_preventPickup(Entity entity, Player player) {
        return !(VanishAPI.isVanished(player) && ConfigManager.vanish().interaction.entityPickup);
    }

    @ModifyReturnValue(
        method = "canBeHitByProjectile",
        at = @At("RETURN")
    )
    public boolean vanish_preventProjectileHits(boolean original) {
        if (ConfigManager.vanish().interaction.entityCollisions && VanishAPI.isVanished((Player) (Object) this)) {
            return false;
        }
        return original;
    }

}
