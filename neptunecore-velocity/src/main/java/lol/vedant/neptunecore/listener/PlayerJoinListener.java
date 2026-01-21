package lol.vedant.neptunecore.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.database.Database;

public class PlayerJoinListener {

    NeptuneCore plugin = NeptuneCore.getInstance();

    @Subscribe
    public void onPlayerJoin(PostLoginEvent e) {
        Player player = e.getPlayer();
        Database database = plugin.getDatabase();

        if(database.getPlayerId(player.getUniqueId()) == -1) {
            database.createPlayer(player.getUniqueId(), player.getUsername());
        }

        plugin.getCache().loadPlayerData(player);
    }

}
