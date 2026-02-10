package lol.vedant.neptunecore.module.chat;

import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.commands.staff.chat.ChatChannelCommand;
import lol.vedant.neptunecore.module.chat.events.PlayerChatListener;
import lol.vedant.neptunecore.module.Module;

public class ChatModule implements Module {

    private NeptuneCore plugin = NeptuneCore.getInstance();
    private ChatManager chatManager;

    @Override
    public void enable() {
        //Require singed velocity plugin to enable on Velocity version 3.4.0+
        //plugin.getServer().getPluginManager().getPlugin("").isPresent()
        //Add integration for LP ranks
        chatManager = new ChatManager();
        NeptuneCore.registerCommand("chat", new ChatChannelCommand(), "channel");
        //Add /sc and /ac commands

        plugin.getServer().getEventManager().register(plugin, new PlayerChatListener());
    }

    @Override
    public void disable() {

    }

    @Override
    public String getName() {
        return "Server Chat";
    }

    public ChatManager getChatManager() {
        return this.chatManager;
    }
}
