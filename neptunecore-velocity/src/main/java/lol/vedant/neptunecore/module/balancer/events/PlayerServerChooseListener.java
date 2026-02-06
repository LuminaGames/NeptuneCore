package lol.vedant.neptunecore.module.balancer.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.module.balancer.BalancerModule;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.List;

public class PlayerServerChooseListener {

    private NeptuneCore plugin = NeptuneCore.getInstance();

    @Subscribe
    public void onPlayerJoin(PlayerChooseInitialServerEvent e) {
        BalancerModule.getBestServer(e.getPlayer()).ifPresent(e::setInitialServer);
    }

    @Subscribe
    public void onPlayerKick(KickedFromServerEvent event) {
        event.getServerKickReason().ifPresent(reason -> {
            List<String> ignoredReasons = plugin.getConfig()
                    .getStringList("ignored-reasons")
                    .stream()
                    .map(String::toLowerCase)
                    .toList();

            String reasonText = PlainTextComponentSerializer.plainText()
                    .serialize(reason)
                    .toLowerCase();

            for (String ignored : ignoredReasons) {
                if (reasonText.contains(ignored)) {
                    return;
                }
            }

            RegisteredServer kickedFrom = event.getServer();

            BalancerModule.getBestServer(event.getPlayer())
                    .ifPresent(server ->
                            event.setResult(
                                    KickedFromServerEvent.RedirectPlayer.create(server)
                            )
                    );
        });
    }

}
