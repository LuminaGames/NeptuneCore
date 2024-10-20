package lol.vedant.neptunecore;

import com.google.inject.Inject;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lol.vedant.neptunecore.commands.player.MessageCommand;
import lol.vedant.neptunecore.commands.player.ReplyCommand;
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
    public static ProxyServer server;

    private ConfigManager configManager;

    private final Path dataFolder;

    @Inject
    public NeptuneCore(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        NeptuneCore.server = server;
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

        CommandManager commandManager = server.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder("message")
                .aliases("msg")
                .plugin(this)
                .build();

        commandManager.register(commandMeta, new MessageCommand());

        CommandMeta replyCommandMeta = commandManager.metaBuilder("reply")
                .aliases("r")
                .plugin(this)
                .build();

        commandManager.register(replyCommandMeta, new ReplyCommand());

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
