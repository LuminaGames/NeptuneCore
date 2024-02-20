package lol.vedant.neptunecore.listeners;

import lol.vedant.neptunecore.managers.AdminChatManager;
import lol.vedant.neptunecore.managers.StaffChatManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerMessageListener implements Listener {

    @EventHandler
    public void onPlayerMessage(ChatEvent e) {
        ProxiedPlayer player = (ProxiedPlayer) e.getSender();
        String message = e.getMessage();
        if(!message.startsWith("/")) {
            if(StaffChatManager.isEnabled(player)) {
                e.setCancelled(true);
                StaffChatManager.sendStaffChat(player, message);
            }
            if (AdminChatManager.isEnabled(player)) {
                e.setCancelled(true);
                AdminChatManager.sendAdminChat(player, message);
            }
        }
    }

}
