package lol.vedant.neptunecore;

import com.google.inject.Inject;
import com.velocitypowered.api.command.*;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lol.vedant.core.data.DatabaseSettings;
import lol.vedant.neptunecore.chat.ChatManager;
import lol.vedant.neptunecore.commands.friend.FriendCommand;
import lol.vedant.neptunecore.commands.staff.ChatChannelCommand;
import lol.vedant.neptunecore.commands.staff.MaintenanceCommand;
import lol.vedant.neptunecore.commands.staff.OnlineStaffCommand;
import lol.vedant.neptunecore.config.ConfigManager;
import lol.vedant.neptunecore.data.Cache;
import lol.vedant.neptunecore.database.Database;
import lol.vedant.neptunecore.database.MySQL;
import lol.vedant.neptunecore.database.SQLite;
import lol.vedant.neptunecore.listener.PlayerChatListener;
import lol.vedant.neptunecore.listener.PlayerJoinListener;
import lol.vedant.neptunecore.listener.ServerPingListener;
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

    private final ConfigManager configManager;
    private final ChatManager chatManager;

    private final Path dataFolder;
    private final Cache cache;

    private final Database database;

    @Inject
    public NeptuneCore(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        NeptuneCore.server = server;
        this.logger = logger;
        this.dataFolder = dataDirectory;


        configManager = new ConfigManager(this);
        chatManager = new ChatManager();

        Message.setConfiguration(getMessage());

        logger.info("Starting Neptune Core (Velocity)");
        logger.info("Developed by Lumina Games");

        logger.info("Initializing Database");
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
            database = new SQLite(dataDirectory.toString());
            database.init();
        }


        //In-Memory Cache
        cache = new Cache(database);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent e) {

        instance = this;

        logger.info("Registering Events");
        registerCommands();
        registerEvents();
        logger.info("Registering Commands");



    }

    public void registerCommand(String name, Object command, String... aliases) {
        CommandManager manager = server.getCommandManager();
        CommandMeta commandMeta = manager.metaBuilder(name)
                .aliases(aliases)
                .plugin(this)
                .build();
        manager.register(commandMeta, (Command) command);
    }

    private void registerEvents() {
        server.getEventManager().register(this, new PlayerChatListener(chatManager));
        server.getEventManager().register(this, new ServerPingListener());
        server.getEventManager().register(this, new PlayerJoinListener(this));
    }

    private void registerCommands() {
        registerCommand("chat", new ChatChannelCommand(), "channel");
        registerCommand("onlinestaff", new OnlineStaffCommand(), "staff");
        registerCommand("friend", FriendCommand.createCommand(server), "f");
        registerCommand("maintenance", MaintenanceCommand.createCommand(server));
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

    public ChatManager getChatManager() {
        return chatManager;
    }

    public Cache getCache() {
        return cache;
    }
}