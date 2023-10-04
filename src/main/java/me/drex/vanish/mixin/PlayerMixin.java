package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @ModifyReturnValue(method = "isInvulnerableTo", at = @At("RETURN"))
    private boolean vanish_invulnerablePlayers(boolean original) {
        if (ConfigManager.vanish().invulnerable && VanishAPI.isVanished((Player) (Object) this)) {
            return true;
        }
        return original;
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
