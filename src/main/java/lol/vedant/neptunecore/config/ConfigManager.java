package lol.vedant.neptunecore.config;

import io.github.vedantmulay.neptuneapi.bungee.config.NeptuneBungeeConfig;
import lol.vedant.neptunecore.NeptuneCore;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;



public class ConfigManager {

    private Plugin plugin;
    private NeptuneBungeeConfig config;
    private NeptuneBungeeConfig messages;
    private NeptuneBungeeConfig servers;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        config = new NeptuneBungeeConfig("config", plugin);
        messages = new NeptuneBungeeConfig("messages", plugin);
        servers = new NeptuneBungeeConfig("servers", plugin);
    }

    public Configuration getConfig() {
        return config.getConfig();
    }

    public Configuration getMessages() {
        return messages.getConfig();
    }

    public Configuration getServers() {
        return servers.getConfig();
    }
    

}
