package me.drex.vanish.mixin;

import com.mojang.authlib.GameProfile;
import eu.pb4.playerdata.api.PlayerDataApi;
import me.drex.vanish.util.VanishData;
import me.drex.vanish.util.VanishedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import static me.drex.vanish.util.VanishManager.VANISH_DATA_STORAGE;

@Mixin(ServerPlayer.class)
public abstract class VanishedServerPlayerMixin extends Player implements VanishedEntity {

    @Shadow
    @Final
    public MinecraftServer server;
    @Unique
    private boolean vanished$dirty = true;

    @Unique
    private boolean vanished$vanished;

    public VanishedServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Override
    public boolean vanish$isVanished() {
        if (vanished$dirty) {
            VanishData data = PlayerDataApi.getCustomDataFor(server, uuid, VANISH_DATA_STORAGE);
            vanished$vanished = data != null && data.vanished;
            vanished$dirty = false;
        }
        return vanished$vanished;
    }

    @Override
    public void vanish$setDirty() {
        vanished$dirty = true;
    }

}
