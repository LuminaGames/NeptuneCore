package lol.vedant.neptunecore;

import lol.vedant.neptunecore.api.friends.FriendManager;
import lol.vedant.neptunecore.commands.friends.FriendCommand;
import lol.vedant.neptunecore.commands.friends.FriendMessageCommand;
import lol.vedant.neptunecore.commands.other.NeptuneCoreCommand;
import lol.vedant.neptunecore.commands.player.*;
import lol.vedant.neptunecore.commands.staff.*;
import lol.vedant.neptunecore.config.ConfigManager;
import lol.vedant.neptunecore.database.Database;
import lol.vedant.neptunecore.database.MySQL;
import lol.vedant.neptunecore.database.SQLite;
import lol.vedant.neptunecore.friends.FriendManagerImpl;
import lol.vedant.neptunecore.listeners.*;
import lol.vedant.neptunecore.metrics.Metrics;
import lol.vedant.neptunecore.utils.Message;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;


public final class NeptuneCore extends Plugin {

    private ConfigManager configManager;
    private FriendManagerImpl friendManager;
    private Database database;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        friendManager = new FriendManagerImpl(this);
        registerCommands();
        registerEvents();

        //Messages Configuration file for messages
        Message.setConfiguration(configManager.getMessages());

        //Initialize Database
        if(getConfig().getBoolean("database.enabled")) {
            database = new MySQL(this);
            database.init();
        } else {
            database = new SQLite(this);
            database.init();
        }

        if(getConfig().getBoolean("metrics.enable-bstats")) {
            Metrics metrics = new Metrics(this, 21393);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Closing Neptune Core");
    }

    public void registerCommands() {
        getProxy().getPluginManager().registerCommand(this, new StaffChatCommand("staffchat", "neptune.command.staffchat", "sc"));
        getProxy().getPluginManager().registerCommand(this, new AdminChatCommand("adminchat", "neptune.command.adminchat", "ac"));
        getProxy().getPluginManager().registerCommand(this, new NeptuneCoreCommand("neptunecore", null,this, "neptune"));
        getProxy().getPluginManager().registerCommand(this, new MessageCommand("message", "neptune.command.message", "msg"));
        getProxy().getPluginManager().registerCommand(this, new OnlineStaffCommand("onlinestaff", "neptune.command.onlinestaff", "staff", "staffonline"));
        getProxy().getPluginManager().registerCommand(this, new ReplyCommand("reply", "neptune.command.reply", "r"));
        getProxy().getPluginManager().registerCommand(this, new LobbyCommand("lobby", "neptune.command.lobby", this, "hub"));
        getProxy().getPluginManager().registerCommand(this, new CommandSpyCommand("commandspy", "neptune.command.commandspy", "cspy"));
        getProxy().getPluginManager().registerCommand(this, new SocialSpyCommand("socialspy", "neptune.command.socialspy", "sspy"));
        getProxy().getPluginManager().registerCommand(this, new MaintenanceCommand("maintenance", "neptune.command.maintenance", this));
        getProxy().getPluginManager().registerCommand(this, new FriendCommand("friend", "neptune.command.friend", this, "f"));
        getProxy().getPluginManager().registerCommand(this, new FriendMessageCommand("friendmessage", "neptune.command.friend", this, "fmsg"));
        getProxy().getPluginManager().registerCommand(this, new PingCommand("ping", "neptune.command.ping"));
    }

    public void registerEvents() {
        getProxy().getPluginManager().registerListener(this, new ServerPingListener(this));
        getProxy().getPluginManager().registerListener(this, new PlayerMessageListener());
        getProxy().getPluginManager().registerListener(this, new ServerKickListener(this));
        getProxy().getPluginManager().registerListener(this, new PrivateMessageListener());
        getProxy().getPluginManager().registerListener(this, new ServerJoinListener(this));
        getProxy().getPluginManager().registerListener(this, new ServerSwitchListener());
    }

    public Configuration getConfig() {
        return configManager.getConfig();
    }

    public Configuration getMessage() {
        return configManager.getMessages();
    }

    public Configuration getServers() {
        return configManager.getServers();
    }

    public void saveConfig() {
        configManager.save();
    }

    public Database getDatabase() {
        return this.database;
    }

    public FriendManager getFriendManager() {
        return friendManager;
    }
}
