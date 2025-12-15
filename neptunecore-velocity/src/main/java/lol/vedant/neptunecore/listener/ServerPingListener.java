package lol.vedant.neptunecore.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import lol.vedant.core.utils.CommonUtil;
import lol.vedant.neptunecore.NeptuneCore;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.List;
import java.util.UUID;

public class ServerPingListener {

    @Subscribe(priority = 0)
    public void onServerPing(ProxyPingEvent e) {
        ConfigurationNode config = NeptuneCore.getInstance().getConfig();
        String motd = config.node("server", "motd", "content").getString();
        ServerPing ping = e.getPing();

        ServerPing.Builder pingBuilder = ping.asBuilder();

        if (config.node("server", "motd", "enabled").getBoolean()) {
            pingBuilder.description(CommonUtil.mm(motd));
        }

        if (config.node("server", "player-hover", "enabled").getBoolean()) {
            List<String> hoverMessage = config.node("server", "player-hover", "content").childrenList().stream()
                    .map(ConfigurationNode::getString)
                    .toList();

            if (hoverMessage.isEmpty()) {
                pingBuilder.samplePlayers(null);
            } else {
                List<ServerPing.SamplePlayer> samplePlayers = hoverMessage.stream()
                        .map(message -> new ServerPing.SamplePlayer(CommonUtil.cc(message), UUID.randomUUID()))
                        .toList();
                pingBuilder.samplePlayers(samplePlayers.toArray(new ServerPing.SamplePlayer[0]));
            }
        }

        e.setPing(pingBuilder.build());
    }

}
