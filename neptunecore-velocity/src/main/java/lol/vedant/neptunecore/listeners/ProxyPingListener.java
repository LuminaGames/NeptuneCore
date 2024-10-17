package lol.vedant.neptunecore.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.proxy.server.ServerPing.SamplePlayer;
import com.google.inject.Inject;
import lol.vedant.core.utils.CommonUtil;
import lol.vedant.neptunecore.NeptuneCore;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

public class ProxyPingListener {

    private final NeptuneCore plugin;

    @Inject
    public ProxyPingListener(NeptuneCore plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onProxyPing(ProxyPingEvent e) {
        ConfigurationNode config = plugin.getConfig();
        String motd = config.node("server", "motd", "content").getString();
        ServerPing ping = e.getPing();

        ServerPing.Builder pingBuilder = ping.asBuilder();

        if (config.node("server", "motd", "enabled").getBoolean()) {
            pingBuilder.description(CommonUtil.mm(motd));
        }

        if (config.node("server", "player-hover", "enabled").getBoolean()) {
            List<String> hoverMessage = StreamSupport.stream(
                            config.node("server", "player-hover", "content").childrenList().spliterator(), false)
                    .map(ConfigurationNode::getString)
                    .toList();

            if (hoverMessage.isEmpty()) {
                pingBuilder.samplePlayers(null);
            } else {
                List<SamplePlayer> samplePlayers = hoverMessage.stream()
                        .map(message -> new SamplePlayer(CommonUtil.cc(message), UUID.randomUUID()))
                        .toList();
                pingBuilder.samplePlayers(samplePlayers.toArray(new SamplePlayer[0]));
            }
        }

        e.setPing(pingBuilder.build());
    }
}
