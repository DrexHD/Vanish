package me.drex.vanish.compat;

import me.drex.vanish.api.VanishEvents;
import net.pl3x.map.core.Pl3xMap;

public class Pl3xmapCompat {

    public static void init() {
        VanishEvents.VANISH_EVENT.register((player, vanish) -> {
            Pl3xMap.api().getPlayerRegistry()
                    .optional(player.getUUID())
                    .ifPresent(playerRegistry -> playerRegistry.setHidden(vanish, true));
        });
    }

}
