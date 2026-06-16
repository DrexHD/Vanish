package me.drex.vanish.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;

import java.util.UUID;

public class VanishBossBar extends BossEvent {
    public static final UUID ID = UUID.fromString("e42a9520-3ae1-44c4-9a35-3d4cfae2d479");
    public static final VanishBossBar INSTANCE = new VanishBossBar();

    private VanishBossBar() {
        super(ID, Component.translatable("text.vanish.general.vanished"), BossBarColor.WHITE, BossBarOverlay.PROGRESS);
        this.setProgress(0);
    }
}
