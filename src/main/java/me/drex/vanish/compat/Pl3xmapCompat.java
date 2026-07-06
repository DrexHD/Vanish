package me.drex.vanish.compat;

import me.drex.vanish.api.VanishEvents;
import net.pl3x.map.core.Pl3xMap;

public class Pl3xmapCompat {

    public static void init() {
        VanishEvents.VANISH_STATUS_CHANGE_EVENT.register((player, server, vanish) -> {
            Pl3xMap.api().getPlayerRegistry()
                    .optional(player)
                    .ifPresent(playerRegistry -> playerRegistry.setHidden(vanish, true));
        });
    }

}
