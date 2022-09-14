package me.drex.vanish.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class VanishEvents {

    /**
     * This event is invoked everytime a players vanish status changes
     */
    public static final Event<VanishEvent> VANISH_EVENT = EventFactory.createArrayBacked(VanishEvent.class, callbacks -> (player, vanish) -> {
        for (var callback : callbacks) {
            callback.onVanish(player, vanish);
        }
    });

    /**
     * This event is invoked to indicate that the player vanished.
     * You may return a custom component to adjust the sent message
     */
    public static final Event<VanishMessageEvent> VANISH_MESSAGE_EVENT = EventFactory.createArrayBacked(VanishMessageEvent.class, callbacks -> (player) -> {
        Component result = Component.empty();
        for (var callback : callbacks) {
            result = callback.getVanishMessage(player);
        }
        return result;
    });

    /**
     * This event is invoked to indicate that the player un-vanished.
     * You may return a custom component to adjust the sent message
     */
    public static final Event<UnVanishMessageEvent> UN_VANISH_MESSAGE_EVENT = EventFactory.createArrayBacked(UnVanishMessageEvent.class, callbacks -> (player) -> {
        Component result = Component.empty();
        for (var callback : callbacks) {
            result = callback.getUnVanishMessage(player);
        }
        return result;
    });

    public interface VanishEvent {
        void onVanish(ServerPlayer player, boolean vanish);
    }

    public interface VanishMessageEvent {
        Component getVanishMessage(ServerPlayer player);
    }

    public interface UnVanishMessageEvent {
        Component getUnVanishMessage(ServerPlayer player);
    }

}
