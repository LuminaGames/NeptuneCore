package lol.vedant.neptunecore.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.database.Database;
import lol.vedant.neptunecore.module.balancer.BalancerModule;

public class PlayerJoinListener {

    NeptuneCore plugin = NeptuneCore.getInstance();

    @Subscribe
    public void onPlayerJoin(PostLoginEvent e) {
        Player player = e.getPlayer();
        Database database = plugin.getDatabase();

        if(!database.exists(player.getUniqueId())) {
            database.createPlayer(player.getUsername(), player.getUniqueId());
        }

        BalancerModule.getBestServer(player);

        plugin.getCache().loadData(player);
    }

}
