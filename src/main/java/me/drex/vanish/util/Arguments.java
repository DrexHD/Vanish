package me.drex.vanish.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class Arguments {
    public static final ThreadLocal<Entity> ACTIVE_ENTITY = ThreadLocal.withInitial(() -> null);
    public static final ThreadLocal<ServerPlayer> PACKET_CONTEXT = ThreadLocal.withInitial(() -> null);
}