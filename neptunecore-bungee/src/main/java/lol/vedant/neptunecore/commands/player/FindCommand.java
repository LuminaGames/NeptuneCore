package lol.vedant.neptunecore.commands.player;

import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.utils.Message;
import lol.vedant.neptunecore.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class FindCommand extends Command {

    private NeptuneCore plugin = NeptuneCore.getInstance();

    public FindCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(!sender.hasPermission("neptune.command.find")) {
            Message.NO_PERMISSION.send(sender);
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(Utils.c("<aqua>Usage: /find <player>"));
            return;
        }

        ProxiedPlayer player = plugin.getProxy().getPlayer(args[0]);

        if(player != null) {
            String serverName = player.getServer().getInfo().getName();

            Message.FIND_MESSAGE.send(
                    sender,
                    "{player}", player.getName(),
                    "{server}", serverName
            );
        } else {
            Message.PLAYER_NOT_FOUND.send(
                    sender,
                    "{player}", args[0]
            );
        }

    }
}
