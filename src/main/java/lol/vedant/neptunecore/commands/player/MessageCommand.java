package lol.vedant.neptunecore.commands.player;

import lol.vedant.neptunecore.managers.MessageManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MessageCommand extends Command {


    public MessageCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length < 2) {
            player.sendMessage(new TextComponent("Usage: /message <playerName> <message>"));
            return;
        }

        String playerName = args[0];
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(playerName);

        if (target == null || !target.isConnected()) {
            player.sendMessage(new TextComponent("Player " + playerName + " is not online."));
            return;
        }

        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }

        MessageManager.message(player, target, message.toString().trim());
    }
}
