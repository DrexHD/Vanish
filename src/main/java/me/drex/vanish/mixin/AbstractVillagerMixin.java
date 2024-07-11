package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractVillager.class)
public class AbstractVillagerMixin {
    @Shadow
    private @Nullable Player tradingPlayer;

    @ModifyReturnValue(method = "isTrading", at = @At("RETURN"))
    private boolean vanish_allowTradingIfVanished(boolean original) {
        if (original) {
            assert this.tradingPlayer != null;
            return !VanishAPI.isVanished(this.tradingPlayer);
        }
        return false;
    }

    @Inject(method = "setTradingPlayer", at = @At("HEAD"))
    private void vanish_closeVanishedPlayerTradeScreen(Player player, CallbackInfo ci) {
        if (player != null && this.tradingPlayer instanceof ServerPlayer serverPlayer) {
            serverPlayer.closeContainer();
        }
    }
}
