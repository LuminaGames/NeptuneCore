package lol.vedant.neptunecore.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.core.utils.CommonUtil;
import lol.vedant.neptunecore.NeptuneCore;

public class ProxyJoinListener {

    private NeptuneCore plugin;

    public ProxyJoinListener(NeptuneCore plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onProxyJoin(PlayerChooseInitialServerEvent e) {
        Player player = e.getPlayer();

        if(!plugin.getDatabase().exists(player.getUniqueId())) {
            plugin.getDatabase().insert(player.getUsername(), player.getUniqueId());
        }

        if(plugin.getConfig().node("maintenance", "enabled").getBoolean() && !player.hasPermission("neptune.maintenance.bypass")) {
            player.disconnect(CommonUtil.mm("Maintenance is on"));
        }
    }

}
