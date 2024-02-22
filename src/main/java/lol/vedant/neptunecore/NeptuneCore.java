package lol.vedant.neptunecore;

import lol.vedant.neptunecore.commands.other.NeptuneCoreCommand;
import lol.vedant.neptunecore.commands.player.MessageCommand;
import lol.vedant.neptunecore.commands.player.OnlineStaffCommand;
import lol.vedant.neptunecore.commands.player.ReplyCommand;
import lol.vedant.neptunecore.commands.staff.AdminChatCommand;
import lol.vedant.neptunecore.commands.staff.StaffChatCommand;
import lol.vedant.neptunecore.config.ConfigManager;
import lol.vedant.neptunecore.listeners.PlayerMessageListener;
import lol.vedant.neptunecore.listeners.ServerPingListener;
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
        // Plugin shutdown logic
    }

    public void registerCommands() {
        getProxy().getPluginManager().registerCommand(this, new StaffChatCommand("staffchat", "neptune.command.staffchat", "sc"));
        getProxy().getPluginManager().registerCommand(this, new AdminChatCommand("adminchat", "neptune.command.adminchat", "ac"));
        getProxy().getPluginManager().registerCommand(this, new NeptuneCoreCommand("neptunecore", null, "neptune"));
        getProxy().getPluginManager().registerCommand(this, new MessageCommand("message", "neptune.command.message", "msg"));
        getProxy().getPluginManager().registerCommand(this, new OnlineStaffCommand("onlinestaff", "neptune.command.onlinestaff", "staff", "staffonline"));
        getProxy().getPluginManager().registerCommand(this, new ReplyCommand("reply", "neptune.command.reply", "r"));
    }

    public void registerEvents() {
        getProxy().getPluginManager().registerListener(this, new ServerPingListener(this));
        getProxy().getPluginManager().registerListener(this, new PlayerMessageListener());
    }

    public Configuration getConfig() {
        return configManager.getConfig();
    }

    public Configuration getMessage() {
        return configManager.getMessages();
    }
}
