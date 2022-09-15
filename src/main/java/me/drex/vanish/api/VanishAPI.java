package me.drex.vanish.api;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.drex.vanish.util.VanishManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public interface VanishAPI {

    /**
     * Check if the specified entity is vanished and should be hidden.
     * Use {@link VanishAPI#canSeePlayer(ServerPlayer, ServerPlayer)},
     * to check which players should be able to see these actions.
     *
     * @param entity the entity to check the status
     * @return the result of the check
     */
    static boolean isVanished(@NotNull Entity entity) {
        if (entity instanceof ServerPlayer player) {
            return isVanished(player.server, player.getUUID());
        }
        return false;
    }

    static boolean isVanished(@NotNull MinecraftServer server, @NotNull UUID uuid) {
        return VanishManager.isVanished(server, uuid);
    }

    /**
     * Set the vanish-status of a player
     *
     * @param player the player whose vanish status should be changed
     * @param status the status to be set (true -> vanish | false -> un-vanish)
     * @return true if the vanish-status changed
     */
    static boolean setVanish(@NotNull ServerPlayer player, boolean status) {
        return VanishManager.setVanished(player, status);
    }

    /**
     * Check whether a player can see the action from another player.
     *
     * @param executive The player who executed the action
     * @param viewer    Is the player that is viewing the action
     * @return the result of the check
     */
    static boolean canSeePlayer(@NotNull ServerPlayer executive, @NotNull ServerPlayer viewer) {
        return canSeePlayer(executive.server, executive.getUUID(), viewer);
    }

    static boolean canSeePlayer(@NotNull MinecraftServer server, @NotNull UUID uuid, @NotNull ServerPlayer viewer) {
        return canSeePlayer(server, uuid, viewer.createCommandSourceStack());
    }

    static boolean canSeePlayer(@NotNull MinecraftServer server, @NotNull UUID uuid, @NotNull CommandSourceStack viewer) {
        return VanishManager.canSeePlayer(server, uuid, viewer);
    }

    /**
     * Returns a list of players that the given {@link CommandSourceStack source} can see
     *
     * @param source the viewing source context
     * @return an immutable list of players that are visible to the source.
     */
    @NotNull
    static List<ServerPlayer> getVisiblePlayers(@NotNull CommandSourceStack source) {
        MinecraftServer server = source.getServer();
        ObjectArrayList<ServerPlayer> list = new ObjectArrayList<>();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (canSeePlayer(server, player.getUUID(), source)) {
                list.add(player);
            }
        }
        return list;
    }

    /**
     * Returns a list of players that can view the actions of the given {@link ServerPlayer player}
     *
     * @param player the player who is observed
     * @return an immutable list of players that can view the player
     */
    @NotNull
    static List<ServerPlayer> getViewingPlayers(@NotNull ServerPlayer player) {
        ObjectArrayList<ServerPlayer> list = new ObjectArrayList<>();
        for (ServerPlayer viewer : player.server.getPlayerList().getPlayers()) {
            if (canSeePlayer(player, viewer)) {
                list.add(viewer);
            }
        }
        return list;
    }

    /**
     * Broadcasts a message that would reveal players vanish status
     * only to players who can see other vanished players
     *
     * @param player    the player who caused the message
     * @param component the component that should be shown
     */
    static void broadcastHiddenMessage(@NotNull ServerPlayer player, @NotNull Component component) {
        component.getSiblings().add(
                Component.translatable("text.vanish.chat.hidden")
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)
        );
        getViewingPlayers(player).forEach(
                viewing -> viewing.sendSystemMessage(component)
        );
    }

}
