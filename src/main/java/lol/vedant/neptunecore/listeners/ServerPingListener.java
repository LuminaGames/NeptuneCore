package lol.vedant.neptunecore.listeners;

import lol.vedant.neptunecore.NeptuneCore;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

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
            ping.setDescriptionComponent(new TextComponent(motd));
        }

        if (plugin.getConfig().getBoolean("server.player-hover.enabled")) {
            //To be done :)
        }
        e.setResponse(ping);
    }
}
