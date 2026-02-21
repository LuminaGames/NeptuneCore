package lol.vedant.neptunecore.module.balancer;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.core.module.Module;
import lol.vedant.neptunecore.module.balancer.events.PlayerServerChooseListener;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BalancerModule implements Module {

    private static NeptuneCore plugin = NeptuneCore.getInstance();

    @Override
    public void enable() {
        plugin.getServer().getEventManager().register(plugin, new PlayerServerChooseListener());
    }

    @Override
    public void disable() {

    }

    @Override
    public String getName() {
        return "Player Balancer";
    }

    public static Optional<RegisteredServer> getBestServer(Player player) {
        List<String> lobbyNames = plugin.getConfig().getStringList("lobby-servers");

        return lobbyNames.stream()
                .map(name -> plugin.getServer().getServer(name))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .min(Comparator.comparingInt(server -> server.getPlayersConnected().size()));
    }
}
