package lol.vedant.neptunecore;

import com.google.inject.Inject;
import com.velocitypowered.api.command.*;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lol.vedant.core.data.DatabaseSettings;
import lol.vedant.core.database.Database;
import lol.vedant.core.database.MySQL;
import lol.vedant.core.database.SQLite;
import lol.vedant.neptunecore.commands.player.FindCommand;
import lol.vedant.neptunecore.commands.player.LobbyCommand;
import lol.vedant.neptunecore.commands.player.MessageCommand;
import lol.vedant.neptunecore.commands.player.ReplyCommand;
import lol.vedant.neptunecore.commands.staff.AdminChatCommand;
import lol.vedant.neptunecore.commands.staff.StaffChatCommand;
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
        version = "1.1.1",
        authors = "COMPHACK"
)
public class NeptuneCore {


    private final Logger logger;
    public static ProxyServer server;
    public static NeptuneCore instance;

    private ConfigManager configManager;

    private final Path dataFolder;

    private Database database;

    @Inject
    public NeptuneCore(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        NeptuneCore.server = server;
        this.logger = logger;
        this.dataFolder = dataDirectory;

        configManager = new ConfigManager(this);

        Message.setConfiguration(getMessage());

        logger.info("Starting Neptune Core (Velocity)");

        logger.info("Initializing Database");
        if(getConfig().node("database", "enabled").getBoolean()) {
            logger.info("Using MySQL Database");
            database = new MySQL(
                    new DatabaseSettings(
                            getConfig().node("database", "host").getString(),
                            getConfig().node("database", "database").getString(),
                            getConfig().node("database", "port").getInt(),
                            getConfig().node("database", "user").getString(),
                            getConfig().node("database", "pass").getString(),
                            getConfig().node("database", "ssl").getBoolean(),
                            getConfig().node("database", "verify-certificate").getBoolean(),
                            getConfig().node("database", "pool-size").getInt(),
                            getConfig().node("database", "max-lifetime").getInt()
                    )
            );
            database.init();
        } else {
            logger.info("Using SQLite Database");
            database = new SQLite(dataDirectory.toString());
            database.init();
        }
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent e) {
        logger.info("Registering Events");
        server.getEventManager().register(this, new ProxyJoinListener(this));
        server.getEventManager().register(this, new ProxyPingListener(this));
        server.getEventManager().register(this, new ServerSwitchListener(this));

        logger.info("Registering Commands");
        registerCommand("message", new MessageCommand(), "msg");
        registerCommand("reply", new ReplyCommand(), "r");
        registerCommand("adminchat", new AdminChatCommand(), "ac");
        registerCommand("staffchat", new StaffChatCommand(), "sc");
        registerCommand("find", new FindCommand());
        registerCommand("lobby", new LobbyCommand(this), "hub");

    }

    public void registerCommand(String name, Object command, String... aliases) {
        CommandManager manager = server.getCommandManager();
        CommandMeta commandMeta = manager.metaBuilder(name)
                .aliases(aliases)
                .plugin(this)
                .build();
        manager.register(commandMeta, (Command) command);
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

    public Database getDatabase() {
        return database;
    }

    public static NeptuneCore getInstance() {
        return instance;
    }
}
