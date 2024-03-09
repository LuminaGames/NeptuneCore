package lol.vedant.neptunecore.listeners;

import lol.vedant.neptunecore.api.events.PrivateMessageEvent;
import lol.vedant.neptunecore.managers.SocialSpyManager;
import lol.vedant.neptunecore.utils.Message;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class PrivateMessageListener implements Listener {

    @EventHandler
    public void onPrivateMessage(PrivateMessageEvent e) {
        List<ProxiedPlayer> staffPlayers = SocialSpyManager.getToggledPlayers();
        ProxiedPlayer sender = e.getSender();
        ProxiedPlayer receiver = e.getReceiver();
        String message = e.getMessage();

        if(staffPlayers.isEmpty()) {
            return;
        }

        for (ProxiedPlayer staff : staffPlayers) {
            Message.SOCIAL_SPY.send(staff,
                    "{sender}", sender.getName(),
                    "{receiver}", receiver.getName(),
                    "{message}", message);
        }
    }

}
