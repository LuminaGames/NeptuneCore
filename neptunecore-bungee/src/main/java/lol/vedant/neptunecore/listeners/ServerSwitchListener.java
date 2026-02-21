package lol.vedant.neptunecore.listeners;

import lol.vedant.neptunecore.utils.Message;
import lol.vedant.neptunecore.utils.Utils;
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

        ServerInfo targetServer = e.getTarget();

        if(player.hasPermission("neptune.staff")) {
            if(e.getPlayer().getServer() == null) {
                for (ProxiedPlayer proxyPlayer : ProxyServer.getInstance().getPlayers()) {
                    if(proxyPlayer.hasPermission("neptune.staff")) {
                        Message.SERVER_JOIN.send(proxyPlayer,
                                "{player}", player.getName(),
                                "{serverName}", Utils.capitalizeFirstLetter(e.getTarget().getName()));
                    }
                }
                return;
            }
            ServerInfo currentServer = e.getPlayer().getServer().getInfo();
            for (ProxiedPlayer proxyPlayer : ProxyServer.getInstance().getPlayers()) {
                if(proxyPlayer.hasPermission("neptune.staff")) {
                    Message.SERVER_SWITCH.send(proxyPlayer,
                            "{player}", player.getName(),
                            "{targetServer}", Utils.capitalizeFirstLetter(targetServer.getName()) ,
                            "{currentServer}", Utils.capitalizeFirstLetter(currentServer.getName()));
                }
            }
        }

    }



}
