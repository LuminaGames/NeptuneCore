package lol.vedant.neptunecore.commands.staff;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import lol.vedant.core.utils.CommonUtil;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.utils.Message;

public class FindCommand implements SimpleCommand {

    NeptuneCore plugin = NeptuneCore.getInstance();

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if(!sender.hasPermission("neptune.staff")) {
            Message.NO_PERMISSION.send(sender);
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(CommonUtil.mm("{prefix} <aqua>Usage: /find <player>"));
            return;
        }

        plugin.getServer().getPlayer(args[0]).ifPresentOrElse(p -> {

            String serverName = p.getCurrentServer()
                    .map(s -> s.getServerInfo().getName())
                    .orElse("Unknown");

            Message.FIND_MESSAGE.send(
                    sender,
                    "{player}", p.getUsername(),
                    "{server}", serverName
            );

        }, () -> {
            Message.PLAYER_NOT_FOUND.send(
                    sender,
                    "{player}", args[0]
            );
        });
    }


    @Override
    public boolean hasPermission(Invocation invocation) {
        return SimpleCommand.super.hasPermission(invocation);
    }
}
