package lol.vedant.neptunecore.config;

import io.github.vedantmulay.neptuneapi.bungee.config.NeptuneBungeeConfig;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;



public class ConfigManager {

    private Plugin plugin;
    private NeptuneBungeeConfig config;
    private NeptuneBungeeConfig messages;
    private NeptuneBungeeConfig servers;
    private NeptuneBungeeConfig friends;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        config = new NeptuneBungeeConfig("config", plugin);
        messages = new NeptuneBungeeConfig("messages", plugin);
        servers = new NeptuneBungeeConfig("servers", plugin);
        friends = new NeptuneBungeeConfig("friends", plugin);
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

    public Configuration getFriends() {
        return friends.getConfig();
    }

    public void save() {
        config.save();
        messages.save();
        servers.save();
        friends.save();
    }
    

}
