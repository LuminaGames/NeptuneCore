package lol.vedant.neptunecore.commands.player;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lol.vedant.neptunecore.module.balancer.BalancerModule;
import lol.vedant.neptunecore.utils.Message;

import java.util.Optional;

public class LobbyCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            Message.PLAYER_ONLY_COMMAND.send(invocation.source());
            return;
        }

        Optional<RegisteredServer> lobby = BalancerModule.getBestServer(player);
        lobby.ifPresent(player::createConnectionRequest);
    }
}
