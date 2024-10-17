package lol.vedant.neptunecore.listeners;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lol.vedant.core.utils.CommonUtil;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.utils.Message;

public class ServerSwitchListener {

    private final NeptuneCore plugin;
    private final ProxyServer proxyServer;

    @Inject
    public ServerSwitchListener(NeptuneCore plugin) {
        this.plugin = plugin;
        proxyServer = plugin.getServer();
    }

    @Subscribe
    public void onServerConnect(ServerConnectedEvent e) {
        Player player = e.getPlayer();
        RegisteredServer targetServer = e.getServer();

        if (player.hasPermission("neptune.staff")) {
            if (player.getCurrentServer().isEmpty()) {
                for (Player proxyPlayer : proxyServer.getAllPlayers()) {
                    if (proxyPlayer.hasPermission("neptune.staff")) {
                        Message.SERVER_JOIN.send(proxyPlayer,
                                "{player}", player.getUsername(),
                                "{serverName}", CommonUtil.capitalizeFirstLetter(targetServer.getServerInfo().getName()));
                    }
                }
                return;
            }

            RegisteredServer currentServer = player.getCurrentServer().get().getServer();
            for (Player proxyPlayer : proxyServer.getAllPlayers()) {
                if (proxyPlayer.hasPermission("neptune.staff")) {
                    Message.SERVER_SWITCH.send(proxyPlayer,
                            "{player}", player.getUsername(),
                            "{targetServer}", CommonUtil.capitalizeFirstLetter(targetServer.getServerInfo().getName()),
                            "{currentServer}", CommonUtil.capitalizeFirstLetter(currentServer.getServerInfo().getName()));
                }
            }
        }
    }

}
