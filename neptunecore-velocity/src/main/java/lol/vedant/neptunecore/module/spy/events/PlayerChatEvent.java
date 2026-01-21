package lol.vedant.neptunecore.module.spy.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.module.staff.StaffModule;

public class PlayerChatEvent {

    @Subscribe
    public void onPlayerChat(com.velocitypowered.api.event.player.PlayerChatEvent e) {
        Player player = e.getPlayer();


    }

}
