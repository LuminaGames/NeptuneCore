package lol.vedant.neptunecore.commands.player;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.ServerInfo;
import lol.vedant.neptunecore.NeptuneCore;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.Random;

import static lol.vedant.neptunecore.NeptuneCore.server;

public class LobbyCommand implements SimpleCommand {

    private NeptuneCore plugin;

    public LobbyCommand(NeptuneCore plugin) {
        this.plugin = plugin;
    }


    @Override
    public void execute(Invocation invocation) {

        if(!(invocation.source() instanceof Player)) {
            return;
        }

        Player player = (Player) invocation.source();

        try {
            List<String> lobbyServers = plugin.getConfig().node("lobby-servers").getList(String.class);
            Random rand = new Random();

            String randomServer = lobbyServers.get(rand.nextInt(lobbyServers.size()));

            server.getServer(randomServer).ifPresent(targetServer -> {
                if (!player.getCurrentServer().map(server -> server.getServerInfo().getName().equalsIgnoreCase(randomServer)).orElse(false)) {
                    player.createConnectionRequest(targetServer);
                }
            });

        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }

    }
}
