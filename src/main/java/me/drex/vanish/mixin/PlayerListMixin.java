package me.drex.vanish.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanish.api.VanishAPI;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @WrapOperation(
            method = "placeNewPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
            )
    )
    public void vanish_hideJoinMessage(PlayerList playerList, Component component, boolean bl, Operation<Void> original, Connection connection, ServerPlayer player) {
        if (VanishAPI.isVanished(player)) {
            VanishAPI.broadcastHiddenMessage(player, component);
        } else {
            original.call(playerList, component, bl);
        }
    }

    @Redirect(
            method = "canPlayerLogin",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/players/PlayerList;players:Ljava/util/List;"
            )
    )
    private List<ServerPlayer> vanish_getNonVanishedPlayerCount(PlayerList playerList) {
        return VanishAPI.getVisiblePlayers(playerList.getServer().createCommandSourceStack().withPermission(0));
    }

}
