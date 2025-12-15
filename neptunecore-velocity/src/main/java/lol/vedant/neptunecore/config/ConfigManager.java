package lol.vedant.neptunecore.config;

import lol.vedant.neptunecore.NeptuneCore;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;

public class ConfigManager {

    private NeptuneCore plugin;
    private final Config config;
    private final Config messages;

    public ConfigManager(NeptuneCore plugin) {
        config = new Config(plugin.getDataFolder(),"config.yml");
        messages = new Config(plugin.getDataFolder(),"messages.yml");
    }

    public YamlFile getConfig() {
        return config.getConfig();
    }

    public YamlFile getMessage() {
        return messages.getConfig();
    }

    public void saveConfig() {
        try {
            config.getConfig().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}