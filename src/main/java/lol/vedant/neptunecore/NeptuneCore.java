package lol.vedant.neptunecore;

import lol.vedant.neptunecore.commands.staff.StaffChatCommand;
import lol.vedant.neptunecore.config.ConfigManager;
import lol.vedant.neptunecore.utils.Message;
import net.md_5.bungee.api.plugin.Plugin;


public final class NeptuneCore extends Plugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        registerCommands();
        Message.setConfiguration(configManager.getMessages());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerCommands() {
        getProxy().getPluginManager().registerCommand(this, new StaffChatCommand("staffchat", "neptune.command.staffchat", "sc"));
        getProxy().getPluginManager().registerCommand(this, new StaffChatCommand("adminchat", "neptune.command.adminchat", "ac"));
    }




}
