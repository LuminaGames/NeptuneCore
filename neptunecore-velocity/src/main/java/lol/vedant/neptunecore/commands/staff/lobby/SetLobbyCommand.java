package lol.vedant.neptunecore.commands.staff.lobby;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.core.utils.CommonUtil;
import lol.vedant.neptunecore.NeptuneCore;

import java.util.List;

public class SetLobbyCommand implements SimpleCommand {

    NeptuneCore plugin = NeptuneCore.getInstance();

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player)) {
            invocation.source().sendPlainMessage("You need to be played to execute this command");
        }

        Player player = (Player) invocation.source();

        List<String> lobbyServers = plugin.getConfig().getStringList("lobby-servers");

        player.getCurrentServer().ifPresent(srv -> {
            String s = srv.getServerInfo().getName();
            if (lobbyServers.contains(s)) {
                player.sendMessage(CommonUtil.mm("<yellow>This server is already set as lobby server. Use <aqua> /removelobby <yellow> to remove this server."));
            } else {
                lobbyServers.add(s);
                plugin.getConfig().set("lobby-servers", lobbyServers);
                player.sendMessage(CommonUtil.mm("<green>Server was added to lobby servers successfully"));
                NeptuneCore.getInstance().saveConfig();
            }
        });
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("neptune.admin.lobby");
    }
}
