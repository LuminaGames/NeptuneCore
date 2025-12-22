package lol.vedant.neptunecore.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import lol.vedant.core.utils.CommonUtil;
import lol.vedant.neptunecore.NeptuneCore;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.List;
import java.util.UUID;

public class ServerPingListener {

    @Subscribe(priority = 0)
    public void onServerPing(ProxyPingEvent e) {
        YamlFile config = NeptuneCore.getInstance().getConfig();
        String motd = config.getString("server.motd.content");
        ServerPing ping = e.getPing();

        ServerPing.Builder pingBuilder = ping.asBuilder();

        if (config.getBoolean("server.motd.enabled")) {
            pingBuilder.description(CommonUtil.mm(motd));
        }

        if (config.getBoolean("server.player-hover.enabled")) {
            List<String> hoverMessage = config.getStringList("server.player-hover.content");

            if (hoverMessage.isEmpty()) {
                pingBuilder.samplePlayers();
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
