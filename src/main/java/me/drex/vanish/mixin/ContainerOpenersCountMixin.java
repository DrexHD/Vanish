package me.drex.vanish.mixin;

import me.drex.vanish.api.VanishAPI;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(ContainerOpenersCounter.class)
public class ContainerOpenersCountMixin {
    @ModifyArg(
        //? if >= 1.21.9-rc1 {
        method = "getEntitiesWithContainerOpen",
        //? } else {
//        method = "getPlayersWithContainerOpen",
        //? }
        at = @At(
            value = "INVOKE",
            //? if >= 1.21.9-rc1 {
            target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            //? } else {
//            target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/level/entity/EntityTypeTest;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            //? }
        ),
        index = 2
    )
    private Predicate<Entity> excludeVanished(Predicate<Entity> predicate) {
        return predicate.and(entity -> !VanishAPI.isVanished(entity));
    }
}
