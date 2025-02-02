package me.drex.vanish;

import me.drex.vanish.api.VanishAPI;
import me.drex.vanish.command.VanishCommand;
import me.drex.vanish.compat.ModCompat;
import me.drex.vanish.config.ConfigManager;
import me.drex.vanish.util.VanishManager;
import me.drex.vanish.util.VanishPlaceHolders;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

public class VanishMod implements ModInitializer {

    public static final String MOD_ID = "vanish";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final ThreadLocal<Entity> ACTIVE_ENTITY = ThreadLocal.withInitial(() -> null);
    public static final Predicate<Entity> NO_SPECTATORS_AND_NO_VANISH = EntitySelector.NO_SPECTATORS.and(entity -> !VanishAPI.isVanished(entity));
    public static final Predicate<Entity> CAN_BE_COLLIDED_WITH_AND_NO_VANISH = NO_SPECTATORS_AND_NO_VANISH.and(Entity::canBeCollidedWith);

    @Override
    public void onInitialize() {
        try {
            ConfigManager.load();
        } catch (Exception e) {
            LOGGER.error("An error occurred while loading the config, keeping default values", e);
        }
        CommandRegistrationCallback.EVENT.register(VanishCommand::register);
        VanishManager.init();
        VanishPlaceHolders.register();
        ModCompat.init();
    }
}
