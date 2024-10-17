package lol.vedant.neptunecore;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lol.vedant.neptunecore.config.ConfigManager;
import lol.vedant.neptunecore.listeners.ProxyJoinListener;
import lol.vedant.neptunecore.listeners.ProxyPingListener;
import lol.vedant.neptunecore.listeners.ServerSwitchListener;
import lol.vedant.neptunecore.utils.Message;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;

import java.nio.file.Path;

@Plugin(
        id = "neptunecore",
        name = "NeptuneCore",
        description = "The all in one core for proxy networks with everything you need...",
        version = "1.1.1"
)
public class NeptuneCore {


    private final Logger logger;
    private final ProxyServer server;

    private ConfigManager configManager;

    private final Path dataFolder;

    @Inject
    public NeptuneCore(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataFolder = dataDirectory;

        configManager = new ConfigManager(this);

        Message.setConfiguration(getMessage());

        logger.info("Starting Neptune Core (Velocity)");
        logger.info(dataDirectory.toString());
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent e) {
        server.getEventManager().register(this, new ProxyJoinListener(this));
        server.getEventManager().register(this, new ProxyPingListener(this));
        server.getEventManager().register(this, new ServerSwitchListener(this));
    }

    public ConfigurationNode getConfig() {
        return configManager.getConfig();
    }

    public ConfigurationNode getMessage() {
        return configManager.getMessage();
    }


    public Path getDataFolder() {
        return dataFolder;
    }

    public ProxyServer getServer() {
        return server;
    }
}
