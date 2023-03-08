package me.drex.vanish.mixin;

import me.drex.vanish.api.VanishAPI;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin {

    @Redirect(
            method = "scanForEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getNearestPlayer(Lnet/minecraft/world/entity/Entity;D)Lnet/minecraft/world/entity/player/Player;"
            )
    )
    private Player vanish_excludeVanished(Level level, Entity experienceOrb, double distance) {
        return level.getNearestPlayer(experienceOrb.getX(), experienceOrb.getY(), experienceOrb.getZ(), distance, EntitySelector.NO_SPECTATORS.and(entity -> !VanishAPI.isVanished(entity)));
    }

}
