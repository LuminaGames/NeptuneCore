package lol.vedant.neptunecore.module.maintenance.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.core.utils.CommonUtil;
import lol.vedant.neptunecore.NeptuneCore;

public class PlayerJoinListener {

    private final NeptuneCore plugin;

    public PlayerJoinListener(NeptuneCore plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPlayerJoin(ServerConnectedEvent e) {
        Player player = e.getPlayer();

        if(plugin.getConfig().getBoolean("maintenance.enabled") && !player.hasPermission("neptune.maintenance.bypass")) {
            player.disconnect(CommonUtil.mm(
                CommonUtil.fromList(NeptuneCore.getInstance().getConfig().getStringList("maintenance.kick-message"))
            ));
            return;
        }

    }
}
