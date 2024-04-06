package lol.vedant.neptunecore;

import lol.vedant.neptunecore.commands.other.NeptuneCoreCommand;
import lol.vedant.neptunecore.commands.player.LobbyCommand;
import lol.vedant.neptunecore.commands.player.MessageCommand;
import lol.vedant.neptunecore.commands.player.OnlineStaffCommand;
import lol.vedant.neptunecore.commands.player.ReplyCommand;
import lol.vedant.neptunecore.commands.staff.*;
import lol.vedant.neptunecore.config.ConfigManager;
import lol.vedant.neptunecore.listeners.*;
import lol.vedant.neptunecore.utils.Message;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;


public final class NeptuneCore extends Plugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        registerCommands();
        registerEvents();
        Message.setConfiguration(configManager.getMessages());

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
}
