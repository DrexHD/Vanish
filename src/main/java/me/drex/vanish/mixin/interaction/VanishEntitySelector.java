package me.drex.vanish.mixin.interaction;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.VanishMod;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.config.ConfigManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.NewMinecartBehavior;
import net.minecraft.world.entity.vehicle.OldMinecartBehavior;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;

public class VanishEntitySelector {

    /**
     * This class is a collection of mixins that (conditionally) replace some {@link EntitySelector#NO_SPECTATORS} with {@link VanishMod#NO_SPECTATORS_AND_NO_VANISH}
     */

    @Mixin(EntityGetter.class)
    public interface EntityGetterMixin {

        @WrapOperation(
            method = "hasNearbyAlivePlayer",
            at = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntitySelector;NO_SPECTATORS:Ljava/util/function/Predicate;"
            )
        )
        default Predicate<Entity> vanish_preventMobSpawning(Operation<Predicate<Entity>> original) {
            return ConfigManager.vanish().interaction.mobSpawning ? VanishMod.NO_SPECTATORS_AND_NO_VANISH : original.call();
        }

        @WrapOperation(
            method = "getNearestPlayer(DDDDZ)Lnet/minecraft/world/entity/player/Player;",
            at = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntitySelector;NO_SPECTATORS:Ljava/util/function/Predicate;"
            )
        )
        default Predicate<Entity> vanish_preventMobSpawning2(Operation<Predicate<Entity>> original) {
            return ConfigManager.vanish().interaction.mobSpawning ? VanishMod.NO_SPECTATORS_AND_NO_VANISH : original.call();
        }
    }

    @Mixin(EntitySelector.class)
    public abstract static class EntitySelectorMixin {

        @WrapOperation(
            method = "pushableBy",
            at = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntitySelector;NO_SPECTATORS:Ljava/util/function/Predicate;"
            )
        )
        private static Predicate<Entity> vanish_preventEntityCollision(Operation<Predicate<Entity>> original, Entity entity) {
            return ConfigManager.vanish().interaction.entityCollisions ? VanishMod.NO_SPECTATORS_AND_NO_VANISH.and(entity1 -> !VanishAPI.isVanished(entity)) : original.call();
        }
    }

    @Mixin(OldMinecartBehavior.class)
    public abstract static class OldMinecartBehaviorMixin {
        @WrapOperation(
            method = "pushAndPickupEntities",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            )
        )
        private List<Entity> vanish_preventMinecartColision(Level instance, Entity entity, AABB aABB, Operation<List<Entity>> original) {
            return ConfigManager.vanish().interaction.entityCollisions ? instance.getEntities(entity, aABB, VanishMod.NO_SPECTATORS_AND_NO_VANISH) : original.call(instance, entity, aABB);
        }
    }

    @Mixin(NewMinecartBehavior.class)
    public abstract static class NewMinecartBehaviorMixin {
        @WrapOperation(
            method = "pushEntities",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            )
        )
        private List<Entity> vanish_preventMinecartColision(Level instance, Entity entity, AABB aABB, Operation<List<Entity>> original) {
            return ConfigManager.vanish().interaction.entityCollisions ? instance.getEntities(entity, aABB, VanishMod.NO_SPECTATORS_AND_NO_VANISH) : original.call(instance, entity, aABB);
        }
    }

    @Mixin(Player.class)
    public abstract static class PlayerMixin {

        @Redirect(
            method = "attack",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            )
        )
        private List<LivingEntity> vanish_preventSweepingEdge(Level instance, Class<LivingEntity> aClass, AABB aabb) {
            return instance.getEntitiesOfClass(aClass, aabb, VanishMod.NO_SPECTATORS_AND_NO_VANISH);
        }
    }

    @Mixin(BeehiveBlock.class)
    public abstract static class BeehiveBlockMixin {

        @Redirect(
            method = "angerNearbyBees",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            )
        )
        private List<LivingEntity> vanish_preventBeeAnger(Level instance, Class<LivingEntity> aClass, AABB aabb) {
            return instance.getEntitiesOfClass(aClass, aabb, VanishMod.NO_SPECTATORS_AND_NO_VANISH);
        }
    }

    @Mixin(BasePressurePlateBlock.class)
    public abstract static class BasePressurePlateBlockMixin {
        @WrapOperation(
            method = "getEntityCount",
            at = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntitySelector;NO_SPECTATORS:Ljava/util/function/Predicate;"
            )
        )
        private static Predicate<Entity> vanish_preventPressurePlatePress(Operation<Predicate<Entity>> original) {
            return ConfigManager.vanish().interaction.blocks ? VanishMod.NO_SPECTATORS_AND_NO_VANISH : original.call();
        }
    }

}
