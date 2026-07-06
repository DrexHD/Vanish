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
                    dynmapCommonAPI.postPlayerJoinQuitToWeb(player.getScoreboardName(), player.getDisplayName().getString(), !vanish);
                });
                VanishEvents.VANISH_STATUS_CHANGE_EVENT.register((player, server, vanish) -> {
                    //? if >= 1.21.9 {
                    server.services().nameToIdCache().get(player).ifPresent(nameAndId -> {
                        dynmapCommonAPI.setPlayerVisiblity(nameAndId.name(), !vanish);
                    });
                    //? } else {
                    /*server.getProfileCache().get(player).ifPresent(gameProfile -> {
                        dynmapCommonAPI.setPlayerVisiblity(gameProfile.getName(), !vanish);
                    });
                    *///? }
                });
            }
        });
    }

}
