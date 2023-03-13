package me.drex.vanish.mixin.interaction;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

@Mixin(EntitySelector.class)
public abstract class EntitySelectorMixin {

    @ModifyReturnValue(method = "pushableBy", at = @At("RETURN"))
    private static Predicate<Entity> vanish_preventEntityCollision(Predicate<Entity> original, Entity pushableBy) {
        return original
                .and(entity -> !(VanishAPI.isVanished(entity) && ConfigManager.vanish().interaction.entityCollisions))
                .and(entity -> !(VanishAPI.isVanished(pushableBy) && ConfigManager.vanish().interaction.entityCollisions));
    }

}
