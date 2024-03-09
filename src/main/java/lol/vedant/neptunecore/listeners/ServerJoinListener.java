package lol.vedant.neptunecore.listeners;

import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.utils.Message;
import lol.vedant.neptunecore.utils.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerJoinListener implements Listener {

    private NeptuneCore plugin;

    public ServerJoinListener(NeptuneCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerJoin(ServerConnectEvent e) {
        ProxiedPlayer player = e.getPlayer();

        for (ProxiedPlayer proxyPlayer : ProxyServer.getInstance().getPlayers()) {
            if(proxyPlayer.hasPermission("neptune.staff.join")) {
                Message.SERVER_SWITCH.send(proxyPlayer,
                        "{player}", player.getName(),
                        "{serverName}", e.getTarget().getName());
            }
        }

        if(plugin.getConfig().getBoolean("maintenance.enabled") && !player.hasPermission("neptune.maintenance.bypass")) {
            player.disconnect(new TextComponent(Utils.cc(Utils.fromList(plugin.getConfig().getStringList("maintenance.kick-message")))));
        }

    }


}
