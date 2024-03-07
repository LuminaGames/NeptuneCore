package lol.vedant.neptunecore.listeners;

import lol.vedant.neptunecore.utils.Message;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerSwitchListener implements Listener {

    @EventHandler
    public void onServerSwitch(ServerConnectEvent e) {
        ProxiedPlayer player = e.getPlayer();
        ServerInfo currentServer = e.getPlayer().getServer().getInfo();
        ServerInfo targetServer = e.getTarget();

        for (ProxiedPlayer proxyPlayer : ProxyServer.getInstance().getPlayers()) {
            if(proxyPlayer.hasPermission("neptune.staff.switch")) {
                Message.SERVER_SWITCH.send(proxyPlayer,
                        "{player}", player.getName(),
                        "{targetServer}", targetServer.getName(),
                        "{currentServer}", currentServer.getName());
            }
        }


    }

}
