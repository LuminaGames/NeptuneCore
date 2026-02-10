package lol.vedant.neptunecore.module.balancer.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.module.balancer.BalancerModule;
import lol.vedant.neptunecore.utils.Message;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.List;

public class PlayerServerChooseListener {

    private NeptuneCore plugin = NeptuneCore.getInstance();

    @Subscribe
    public void onPlayerJoin(PlayerChooseInitialServerEvent e) {
        BalancerModule.getBestServer(e.getPlayer()).ifPresent(e::setInitialServer);
    }

    @Subscribe
    public void onPlayerKick(KickedFromServerEvent e) {
        e.getServerKickReason().ifPresent(reason -> {
            List<String> ignoredReasons = plugin.getConfig().getStringList("ignored-reasons");

            String reasonText = PlainTextComponentSerializer.plainText()
                    .serialize(reason)
                    .toLowerCase();

            for (String ignored : ignoredReasons) {
                if (reasonText.contains(ignored)) {
                    return;
                }
            }

            RegisteredServer kickedFrom = e.getServer();

            BalancerModule.getBestServer(e.getPlayer()).ifPresent(server -> {
                e.setResult(KickedFromServerEvent.RedirectPlayer.create(server));
                Message.FALLBACK_CONNECTION.send(e.getPlayer());
            });

        });
    }

}
