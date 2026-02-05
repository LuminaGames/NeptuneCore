package lol.vedant.neptunecore.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.NeptuneCore;

public class PlayerDisconnectListener {

    private final NeptuneCore plugin;

    public PlayerDisconnectListener(NeptuneCore plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPlayerDisconnect(DisconnectEvent e) {
        Player player = e.getPlayer();

        //Clear cache from server memory
        plugin.getCache().remove(player);
    }

}
