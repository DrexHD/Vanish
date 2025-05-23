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
    private Component vanish$cachedDisplayName = null;

    @ModifyReturnValue(method = "isInvulnerableTo", at = @At("RETURN"))
    private boolean invulnerablePlayers(boolean original) {
        if (ConfigManager.vanish().invulnerable && VanishAPI.isVanished((Player) (Object) this)) {
            return true;
        }
        return original;
    }

    @WrapMethod(method = "getDisplayName")
    public Component cacheDisplayName(Operation<Component> original) {
        if (vanish$cachedDisplayName == null) {
            vanish$cachedDisplayName = original.call();
        }
        return vanish$cachedDisplayName;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void invalidateCache(CallbackInfo ci) {
        vanish$cachedDisplayName = null;
    }

}
