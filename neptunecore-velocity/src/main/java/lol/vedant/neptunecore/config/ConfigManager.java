package lol.vedant.neptunecore.config;

import lol.vedant.neptunecore.NeptuneCore;
import org.spongepowered.configurate.ConfigurationNode;

public class ConfigManager {

    private NeptuneCore plugin;
    private final Config config;
    private final Config messages;

    public ConfigManager(NeptuneCore plugin) {
        config = new Config(plugin.getDataFolder(), "config.yml");
        messages = new Config(plugin.getDataFolder(), "messages.yml");
    }

    public ConfigurationNode getConfig() {
        return config.getConfig();
    }

    public ConfigurationNode getMessage() {
        return messages.getConfig();
    }
}