package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow
    public abstract CommandSourceStack createCommandSourceStack();

    @Redirect(
            method = "tickServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;getPlayerCount()I"
            )
    )
    public int vanish_getNonVanishedPlayerCount(MinecraftServer server) {
        return VanishAPI.getVisiblePlayers(this.createCommandSourceStack().withPermission(0)).size();
    }

    @ModifyReceiver(
            method = "tickServer",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;get(I)Ljava/lang/Object;"
            )
    )
    public List<ServerPlayer> vanish_getNonVanishedPlayer(List<ServerPlayer> original, int index) {
        return VanishAPI.getVisiblePlayers(this.createCommandSourceStack().withPermission(0));
    }

}
