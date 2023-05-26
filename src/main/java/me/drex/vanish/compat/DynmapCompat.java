package me.drex.vanish.compat;

import me.drex.vanish.api.VanishEvents;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;

public class DynmapCompat {

    public static void init() {
        DynmapCommonAPIListener.register(new DynmapCommonAPIListener() {
            @Override
            public void apiEnabled(DynmapCommonAPI dynmapCommonAPI) {
                VanishEvents.VANISH_EVENT.register((player, vanish) -> {
                    dynmapCommonAPI.setPlayerVisiblity(player.getScoreboardName(), !vanish);
                    dynmapCommonAPI.postPlayerJoinQuitToWeb(player.getScoreboardName(), player.getDisplayName().getString(), !vanish);
                });
            }
        });
    }

}
