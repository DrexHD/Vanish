package me.drex.vanish.mixin;

import me.drex.vanish.api.VanishAPI;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EntitySelector.class)
public abstract class EntitySelectorMixin {

    @Inject(method = "findPlayers", at = @At("RETURN"))
    public void vanish_removeVanishedPlayers(CommandSourceStack src, CallbackInfoReturnable<List<ServerPlayer>> cir) {
        List<ServerPlayer> players = cir.getReturnValue();
        Entity entity = src.getEntity();
        if (entity instanceof ServerPlayer viewer) {
            players.removeIf((executor) -> !VanishAPI.canSeePlayer(executor, viewer));
        }
    }

}
