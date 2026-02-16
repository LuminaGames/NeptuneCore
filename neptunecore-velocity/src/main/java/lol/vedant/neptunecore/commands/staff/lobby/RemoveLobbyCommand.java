package lol.vedant.neptunecore.commands.staff.lobby;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.core.utils.CommonUtil;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.utils.Message;

import java.io.IOException;
import java.util.List;

public class RemoveLobbyCommand implements SimpleCommand {

    NeptuneCore plugin = NeptuneCore.getInstance();

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player)) {
            invocation.source().sendPlainMessage("You need to be player to execute this command");
        }

        Player player = (Player) invocation.source();

        if(!player.hasPermission("neptune.admin.lobby")) {
            Message.NO_PERMISSION.send(player);
            return;
        }

        List<String> lobbyServers = plugin.getConfig().getStringList("lobby-servers");

        player.getCurrentServer().ifPresent(srv -> {
            String s = srv.getServerInfo().getName();
            if (lobbyServers.contains(s)) {
                lobbyServers.remove(s);
                plugin.getConfig().set("lobby-servers", lobbyServers);
                player.sendMessage(CommonUtil.mm("<green>Server was removed from lobby servers successfully"));
                NeptuneCore.getInstance().saveConfig();
            } else {
                player.sendMessage(CommonUtil.mm("<yellow>This server is not set as lobby server. Use <aqua> /setlobby <yellow>to add this server."));

            }
        });
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("neptune.admin.lobby");
    }
}
