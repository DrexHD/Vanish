package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Unique
    private Component vanish_cachedDisplayName = null;

    @ModifyReturnValue(method = "isInvulnerableTo", at = @At("RETURN"))
    private boolean vanish_invulnerablePlayers(boolean original) {
        if (ConfigManager.vanish().invulnerable && VanishAPI.isVanished((Player) (Object) this)) {
            return true;
        }
        return original;
    }

    @WrapMethod(method = "getDisplayName")
    public Component vanish_cacheDisplayName(Operation<Component> original) {
        if (vanish_cachedDisplayName == null) {
            vanish_cachedDisplayName = original.call();
        }
        return vanish_cachedDisplayName;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void vanish_invalidateCache(CallbackInfo ci) {
        vanish_cachedDisplayName = null;
    }

}