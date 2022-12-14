package me.drex.vanish;

import me.drex.vanish.command.VanishCommand;
import me.drex.vanish.compat.ModCompat;
import me.drex.vanish.config.ConfigManager;
import me.drex.vanish.util.VanishManager;
import me.drex.vanish.util.VanishPlaceHolders;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VanishMod implements DedicatedServerModInitializer {

    public static final String MOD_ID = "vanish";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeServer() {
        try {
            ConfigManager.INSTANCE.load();
        } catch (Exception e) {
            LOGGER.error("An error occurred while loading the config, keeping default values", e);
        }
        CommandRegistrationCallback.EVENT.register(VanishCommand::register);
        VanishManager.init();
        VanishPlaceHolders.register();
        ModCompat.init();
    }
}
