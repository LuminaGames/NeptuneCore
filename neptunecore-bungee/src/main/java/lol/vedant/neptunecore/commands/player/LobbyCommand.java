package lol.vedant.neptunecore.commands.player;

import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.utils.Message;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LobbyCommand extends Command {

    private NeptuneCore plugin;

    public LobbyCommand(String name, String permission, NeptuneCore plugin, String... aliases) {
        super(name, permission, aliases);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer)) {
            Message.PLAYER_ONLY_COMMAND.send(sender);
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        List<String> servers = new ArrayList<>(plugin.getConfig().getStringList("lobby-servers"));

        Random rand = new Random();

        String server = servers.get(rand.nextInt(servers.size()));

        ServerInfo fallback = ProxyServer.getInstance().getServerInfo(server);

        if (player.getServer().getInfo().getName().equalsIgnoreCase(fallback.getName())) {
            return;
        }

        player.connect(fallback);


    }
}