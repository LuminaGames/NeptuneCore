package lol.vedant.neptunecore.listeners;

import lol.vedant.neptunecore.NeptuneCore;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.List;

public class ServerKickListener implements Listener {

    private NeptuneCore plugin;

    public ServerKickListener(NeptuneCore plugin) {
        this.plugin = plugin;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerKick(ServerKickEvent e) {
        ProxiedPlayer player = e.getPlayer();
        ServerInfo kicked = e.getKickedFrom();

        if(!player.isConnected()) {
            return;
        }

        boolean isEmpty = e.getKickReasonComponent() == null;
        String reason = isEmpty ? "" : BaseComponent.toLegacyText(e.getKickReasonComponent());
        List<String> ignoredReasons = plugin.getConfig().getStringList("ignored-reasons");
        List<String> ignoredServers = plugin.getConfig().getStringList("ignored-servers");

        for (String iReason : ignoredReasons) {
            if (reason.contains(iReason)) {
                return;
            }
        }

        for (String iServer : ignoredServers) {
            if(kicked.getName().equalsIgnoreCase(iServer)) {
                return;
            }
        }


        






    }

}
