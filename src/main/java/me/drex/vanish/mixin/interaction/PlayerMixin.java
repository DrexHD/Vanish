package me.drex.vanish.mixin.interaction;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @WrapWithCondition(method = "touch", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;playerTouch(Lnet/minecraft/world/entity/player/Player;)V"))
    private boolean vanish_preventPickup(Entity entity, Player player) {
        return !VanishAPI.isVanished(player);
    }

}
