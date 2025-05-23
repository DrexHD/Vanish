package me.drex.vanish.mixin;

import me.drex.vanish.api.VanishAPI;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.ListPlayersCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ListPlayersCommand.class)
public abstract class ListPlayersCommandMixin {

    @Redirect(
        method = "format",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;getPlayers()Ljava/util/List;"
        )
    )
    private static List<ServerPlayer> removeVanishedPlayers(PlayerList playerList, CommandSourceStack observer) {
        return VanishAPI.getVisiblePlayers(observer);
    }

}
