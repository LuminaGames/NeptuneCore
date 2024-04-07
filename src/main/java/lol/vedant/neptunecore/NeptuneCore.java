package lol.vedant.neptunecore;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lol.vedant.neptunecore.config.ConfigManager;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;

import java.nio.file.Path;

@Plugin(
        id = "neptunecore",
        name = "NeptuneCore",
        version = "1.0.0",
        description = "The all-in-one core for your proxy server!",
        url = "https://vedant.lol",
        authors = {"COMPHACK"}
)
public class NeptuneCore {


    private final Logger logger;
    private final ProxyServer server;
    private final Path dataFolder;

    private ConfigManager configManager;

    @Inject
    public NeptuneCore(ProxyServer server, Logger logger, @DataDirectory Path dataFolder) {
        this.server = server;
        this.logger = logger;
        this.dataFolder = dataFolder;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        configManager = new ConfigManager(this);
    }

    public Path getDataFolder() {
        return this.dataFolder;
    }

    public Logger getLogger() {
        return logger;
    }

    public ConfigurationNode getConfig() {
        return configManager.getConfig();
    }

    public ConfigurationNode getMessage() {
        return configManager.getMessage();
    }
}
