package me.drex.vanish.config;

import me.drex.vanish.VanishMod;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.nio.file.Path;

public class ConfigManager {

    private static final Logger LOGGER = VanishMod.LOGGER;
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("vanish.hocon");
    public static final ConfigManager INSTANCE = new ConfigManager();
    private VanishConfig vanishConfig = new VanishConfig();

    private ConfigManager() {
    }

    public void load() throws Exception {
        LOGGER.info("Loading configuration...");
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder().path(CONFIG_FILE).build();
        CommentedConfigurationNode rootNode = loader.load();
        if (!CONFIG_FILE.toFile().exists()) {
            LOGGER.info("Creating configuration file!");
            rootNode.set(VanishConfig.class, new VanishConfig());
            loader.save(rootNode);
        }
        vanishConfig = rootNode.get(VanishConfig.class, new VanishConfig());
    }

    public VanishConfig vanish() {
        return vanishConfig;
    }

}
