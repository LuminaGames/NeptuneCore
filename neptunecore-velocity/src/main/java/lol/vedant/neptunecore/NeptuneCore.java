package lol.vedant.neptunecore;

import com.google.inject.Inject;
import com.velocitypowered.api.command.*;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lol.vedant.core.data.DatabaseSettings;
import lol.vedant.neptunecore.commands.friend.FriendCommand;
import lol.vedant.neptunecore.commands.staff.OnlineStaffCommand;
import lol.vedant.neptunecore.config.ConfigManager;
import lol.vedant.neptunecore.data.Cache;
import lol.vedant.neptunecore.database.Database;
import lol.vedant.neptunecore.database.MySQL;
import lol.vedant.neptunecore.database.SQLite;
import lol.vedant.neptunecore.listener.PlayerDisconnectListener;
import lol.vedant.neptunecore.listener.PlayerJoinListener;
import lol.vedant.neptunecore.listener.ServerPingListener;
import lol.vedant.neptunecore.module.ModuleManager;
import lol.vedant.neptunecore.utils.Message;
import org.simpleyaml.configuration.file.YamlFile;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "neptunecore",
        name = "NeptuneCore",
        description = "The all in one core for proxy networks with everything you need...",
        version = "2.0.0",
        authors = "COMPHACK"
)
public class NeptuneCore {


    private final Logger logger;
    public static ProxyServer server;
    public static NeptuneCore instance;

    private ConfigManager configManager;
    private ModuleManager moduleManager;

    private final Path dataFolder;
    private Cache cache;

    private Database database;

    @Inject
    public NeptuneCore(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        NeptuneCore.server = server;
        this.logger = logger;
        this.dataFolder = dataDirectory;

        logger.info("Starting Neptune Core (Velocity)");
        logger.info("Developed by Lumina Games");
        //In-Memory Cache

    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent e) {

        instance = this;

        moduleManager = new ModuleManager();
        configManager = new ConfigManager(this);

        Message.setConfiguration(getMessage());

        if(getConfig().getBoolean("database.enabled")) {
            logger.info("Using MySQL Database");
            database = new MySQL(
                    new DatabaseSettings(
                            getConfig().getString("database.host"),
                            getConfig().getString("database.database"),
                            getConfig().getInt("database.port"),
                            getConfig().getString("database.user"),
                            getConfig().getString("database.pass"),
                            getConfig().getBoolean("database.ssl"),
                            getConfig().getBoolean("database.verify-certificate"),
                            getConfig().getInt("database.pool-size"),
                            getConfig().getInt("database.max-lifetime")
                    )
            );
            database.init();
        } else {
            logger.info("Using SQLite Database");
            database = new SQLite(dataFolder.toString());
            database.init();
        }

        logger.info("Registering Events");
        registerCommands();
        registerEvents();
        logger.info("Registering Commands");

        cache = new Cache(database);
    }

    public static void registerCommand(String name, Object command, String... aliases) {
        CommandManager manager = server.getCommandManager();
        CommandMeta commandMeta = manager.metaBuilder(name)
                .aliases(aliases)
                .plugin(instance)
                .build();
        manager.register(commandMeta, (Command) command);
    }

    private void registerEvents() {
        server.getEventManager().register(this, new PlayerJoinListener());
        server.getEventManager().register(this, new ServerPingListener());
        server.getEventManager().register(this, new PlayerDisconnectListener(this));
    }

    private void registerCommands() {

        registerCommand("onlinestaff", new OnlineStaffCommand(), "staff");
        registerCommand("friend", FriendCommand.createCommand(server), "f");

    }

    public YamlFile getConfig() {
        return configManager.getConfig();
    }

    public YamlFile getMessage() {
        return configManager.getMessage();
    }

    public void saveConfig() {
        configManager.saveConfig();
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

    public Cache getCache() {
        return cache;
    }
}