package lol.vedant.neptunecore.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.api.events.PrivateMessageEvent;
import lol.vedant.neptunecore.managers.SocialSpyManager;
import lol.vedant.neptunecore.utils.Message;

import java.util.List;

public class PrivateMessageListener {

    @Subscribe
    public void onPrivateMessage(PrivateMessageEvent e) {
        List<Player> staffPlayers = SocialSpyManager.getToggledPlayers();
        Player sender = e.getSender();
        Player receiver = e.getReceiver();
        String message = e.getMessage();

        if(staffPlayers.isEmpty()) {
            return;
        }

        for (Player staff : staffPlayers) {
            Message.SOCIAL_SPY.send(staff,
                    "{sender}", sender.getUsername(),
                    "{receiver}", receiver.getUsername(),
                    "{message}", message);
        }
    }
}
