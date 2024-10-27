package lol.vedant.neptunecore.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import lol.vedant.neptunecore.managers.PlayerDataManager;


public class ProxyDisconnectListener {

    @Subscribe
    public void onPlayerDisconnect(DisconnectEvent e) {

        //Remove data
        PlayerDataManager.removeData(e.getPlayer());
    }

}
