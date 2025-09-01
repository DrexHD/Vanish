package me.drex.vanish.mixin;

import me.drex.vanish.api.VanishAPI;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(ContainerOpenersCounter.class)
public class ContainerOpenersCountMixin {

    @ModifyArg(
            method = "getOpenCount(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/level/entity/EntityTypeTest;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            ),
            index = 2
    )
    private Predicate<? super Player> vanish_excludeVanished(Predicate<? super Player> original) {
        // Keep vanilla's isOwnContainer() checks and just add "not vanished"
        return original.and(player -> !VanishAPI.isVanished((Entity) player));
    }
}
