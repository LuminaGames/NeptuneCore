package lol.vedant.neptunecore.commands.player;

import lol.vedant.neptunecore.NeptuneCore;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ServerConnectRequest;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class LobbyCommand extends Command {

    private NeptuneCore plugin;

    public LobbyCommand(String name, String permission, NeptuneCore plugin, String... aliases) {
        super(name, permission, aliases);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        List<String> lobbyServers = plugin.getConfig().getStringList("fallback-servers");
        ProxiedPlayer player = (ProxiedPlayer) sender;
        ServerInfo targetServer = null;
        int minPlayers = Integer.MAX_VALUE;

        for (ServerInfo server : plugin.getProxy().getServers().values()) {
            if (lobbyServers.contains(server.getName())) {
                int playerCount = server.getPlayers().size();
                if (playerCount < minPlayers) {
                    minPlayers = playerCount;
                    targetServer = server;
                }
            }
        }

        if (targetServer != null) {
            player.connect(targetServer);
            player.sendMessage("You have been sent to the lobby server with the lowest player count.");
        } else {
            player.sendMessage("No lobby servers are available at the moment.");
        }
    }
}