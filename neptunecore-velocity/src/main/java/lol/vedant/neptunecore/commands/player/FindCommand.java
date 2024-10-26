package lol.vedant.neptunecore.commands.player;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.core.utils.CommonUtil;
import lol.vedant.neptunecore.utils.Message;

import static lol.vedant.neptunecore.NeptuneCore.server;

public class FindCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if(!(invocation.source() instanceof Player)) {
            return;
        }

        Player player = (Player) invocation.source();
        String[] args = invocation.arguments();

        if(args.length < 1) {
            player.sendMessage(CommonUtil.mm("<red>Please provide a username to find in the server"));
            return;
        }

        String targetUser = args[0];

        server.getPlayer(targetUser)
                .flatMap(Player::getCurrentServer)
                .ifPresentOrElse(
                        serverConnection -> player.sendMessage(CommonUtil.mm("<aqua>" + targetUser + "<white> is online at <aqua>" + serverConnection.getServerInfo().getName())),
                        () -> Message.OFFLINE_PLAYER.send(player)
                );

    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("neptune.command.find");
    }
}
