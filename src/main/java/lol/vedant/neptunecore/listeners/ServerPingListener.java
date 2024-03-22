package lol.vedant.neptunecore.listeners;

import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.utils.Utils;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.List;
import java.util.UUID;


public class ServerPingListener implements Listener {

    private NeptuneCore plugin;

    public ServerPingListener(NeptuneCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void serverPingEvent(ProxyPingEvent e) {
        String motd = plugin.getConfig().getString("server.motd.content");
        final ServerPing ping = e.getResponse();

        if (plugin.getConfig().getBoolean("server.motd.enabled")) {
            ping.setDescriptionComponent(new TextComponent(Utils.cc(motd)));
        }

        if (plugin.getConfig().getBoolean("server.player-hover.enabled")) {
            List<String> hoverMessage = plugin.getConfig().getStringList("server.player-hover.content");

            if(hoverMessage.isEmpty()) {
                ping.getPlayers().setSample(null);
            } else {
                ServerPing.PlayerInfo[] info = new ServerPing.PlayerInfo[hoverMessage.size()];
                for (int i = 0; i < info.length; i++) {
                    info[i] = new ServerPing.PlayerInfo(Utils.cc(hoverMessage.get(i)), UUID.randomUUID());
                }
                ping.getPlayers().setSample(info);
            }

        }
        e.setResponse(ping);
    }
}
