package me.drex.vanish.api;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.drex.vanish.util.VanishManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
        return VanishManager.isVanished(entity);
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
        return VanishManager.setVanished(player.getGameProfile(), player.level().getServer(), status);
    }

    /**
     * Check whether a player can see the action from another player.
     *
     * @param actor    The player who executed the action
     * @param observer Is the player that is viewing the action
     * @return the result of the check
     */
    static boolean canSeePlayer(@NotNull ServerPlayer actor, @NotNull ServerPlayer observer) {
        return VanishManager.canSeePlayer(actor, observer.createCommandSourceStack());
    }

    static boolean canSeePlayer(@NotNull MinecraftServer server, @NotNull UUID uuid, @NotNull ServerPlayer observer) {
        return canSeePlayer(server, uuid, observer.createCommandSourceStack());
    }

    static boolean canSeePlayer(@NotNull MinecraftServer server, @NotNull UUID uuid, @NotNull CommandSourceStack observer) {
        return VanishManager.canSeePlayer(server, uuid, observer);
    }

    static boolean canViewVanished(SharedSuggestionProvider observer) {
        return VanishManager.canViewVanished(observer);
    }

    /**
     * Returns a list of players that the given {@link CommandSourceStack source} can see
     *
     * @param observer the viewing source context
     * @return an immutable list of players that are visible to the source.
     */
    @NotNull
    static List<ServerPlayer> getVisiblePlayers(@NotNull CommandSourceStack observer) {
        MinecraftServer server = observer.getServer();
        ObjectArrayList<ServerPlayer> list = new ObjectArrayList<>();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (VanishManager.canSeePlayer(player, observer)) {
                list.add(player);
            }
        }
        return list;
    }

    /**
     * Returns a list of players that can view the actions of the given {@link ServerPlayer}
     *
     * @param actor the player who is observed
     * @return an immutable list of players that can view the player
     */
    @NotNull
    @Deprecated(forRemoval = true)
    static List<ServerPlayer> getViewingPlayers(@NotNull ServerPlayer actor) {
        ObjectArrayList<ServerPlayer> list = new ObjectArrayList<>();
        for (ServerPlayer observer : actor.level().getServer().getPlayerList().getPlayers()) {
            if (canSeePlayer(actor, observer)) {
                list.add(observer);
            }
        }
        return list;
    }

    /**
     * Broadcasts a message that would reveal players vanish status
     * only to players who can see other vanished players
     *
     * @param actor   the player who caused the message
     * @param message the message that should be shown
     */
    static void broadcastHiddenMessage(@NotNull ServerPlayer actor, @NotNull Component message) {
        MutableComponent component = message.copy();
        component.append(Component.translatable("text.vanish.chat.hidden")
            .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        for (ServerPlayer observer : actor.level().getServer().getPlayerList().getPlayers()) {
            if (canSeePlayer(actor, observer)) {
                observer.sendSystemMessage(component);
            }
        }
    }

    /**
     * Conditionally send a message that would reveal players vanish status
     * only if the observer can see other vanished players
     *
     * @param actor    the player who caused the message
     * @param observer the player who could receive the message
     * @param message  the message that should be shown
     */
    static void sendHiddenMessage(@NotNull ServerPlayer actor, @NotNull ServerPlayer observer, @NotNull Component message) {
        MutableComponent component = message.copy();
        component.append(Component.translatable("text.vanish.chat.hidden")
            .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        if (canSeePlayer(actor, observer)) {
            observer.sendSystemMessage(component);
        }
    }

}
