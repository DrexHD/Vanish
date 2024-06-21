package me.drex.vanish.mixin;

import me.drex.vanish.api.VanishAPI;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(ContainerOpenersCounter.class)
public class ContainerOpenersCountMixin {
    @ModifyArg(
            method = "getPlayersWithContainerOpen",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/level/entity/EntityTypeTest;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"),
            index = 2
    )
    private Predicate<Player> vanish_cancelCountChange(Predicate<Player> predicate) {
        return predicate.and(player -> !VanishAPI.isVanished(player));
    }
}
