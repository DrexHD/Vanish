package me.drex.vanish.mixin.predicate;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.util.predicate.IEntityFlagsPredicate;
//? if >= 26.2 {
import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
//? } else {
/*import net.minecraft.advancements.criterion.EntityFlagsPredicate;
*///? }
import net.minecraft.world.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EntityFlagsPredicate.class)
public abstract class EntityFlagsFlagsPredicateMixin implements IEntityFlagsPredicate {
    @Shadow
    @Mutable
    public static Codec<EntityFlagsPredicate> CODEC;

    @Unique
    Optional<Boolean> vanish$isVisible;

    @Override
    public void vanish$setVisible(Optional<Boolean> visible) {
        vanish$isVisible = visible;
    }

    @Override
    public Optional<Boolean> vanish$isVisible() {
        return vanish$isVisible;
    }

    @WrapOperation(
        method = "<clinit>",
        at = @At(
            value = "FIELD",
            //? if >= 26.2 {
            target = "Lnet/minecraft/advancements/predicates/entity/EntityFlagsPredicate;CODEC:Lcom/mojang/serialization/Codec;",
            //? } else {
            /*target = "Lnet/minecraft/advancements/critereon/EntityFlagsPredicate;CODEC:Lcom/mojang/serialization/Codec;",
            *///? }
            opcode = Opcodes.PUTSTATIC
        )
    )
    private static void addVanishFlag(Codec<EntityFlagsPredicate> codec, Operation<Void> original) {
        MapCodec<EntityFlagsPredicate> originalMap = ((MapCodec.MapCodecCodec<EntityFlagsPredicate>) codec).codec();

        MapCodec<Optional<Boolean>> extrasMap = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("vanish:is_visible").forGetter(o -> o)
        ).apply(instance, o -> o));

        MapCodec<EntityFlagsPredicate> combined = Codec.mapPair(originalMap, extrasMap).xmap(
            pair -> {
                EntityFlagsPredicate predicate = pair.getFirst();
                Optional<Boolean> isVanished = pair.getSecond();
                IEntityFlagsPredicate extraFlags = (IEntityFlagsPredicate) (Object) predicate;
                extraFlags.vanish$setVisible(isVanished);
                return predicate;
            },
            predicate -> {
                IEntityFlagsPredicate extraFlags = (IEntityFlagsPredicate) (Object) predicate;
                return Pair.of(predicate, extraFlags.vanish$isVisible());
            }
        );
        original.call(combined.codec());
    }

    @Inject(
        method = "matches(Lnet/minecraft/world/entity/Entity;)Z",
        at = @At("TAIL"),
        cancellable = true
    )
    public void addVanishFlag(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (vanish$isVisible.isPresent() && VanishAPI.isVanished(entity) == vanish$isVisible.get()) {
            cir.setReturnValue(false);
        }
    }
}
