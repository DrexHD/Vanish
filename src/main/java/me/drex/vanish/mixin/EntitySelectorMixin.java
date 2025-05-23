package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedList;
import java.util.List;

@Mixin(EntitySelector.class)
public abstract class EntitySelectorMixin {

    @ModifyReturnValue(method = "findPlayers", at = @At("RETURN"))
    public List<ServerPlayer> removeVanishedPlayers(List<ServerPlayer> original, CommandSourceStack src) {
        List<ServerPlayer> players = new LinkedList<>(original);
        ServerPlayer observer = src.getPlayer();
        if (observer != null) {
            players.removeIf((actor) -> !VanishAPI.canSeePlayer(actor, observer));
        }
        return players;
    }

}
