package lol.vedant.neptunecore.commands.player;

import lol.vedant.neptunecore.managers.MessageManager;
import lol.vedant.neptunecore.utils.Message;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReplyCommand extends Command {

    public ReplyCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        String message = String.join(" ", args);
        if(MessageManager.hasLastMessage(player)) {
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(MessageManager.getLastMessage(player).getUniqueId());
            if(target != null && target.isConnected()) {
                MessageManager.message(player, MessageManager.getLastMessage(player), message);
            } else {
                Message.OFFLINE_PLAYER.send(player);
            }
        } else {
            Message.NO_ONE_REPLY.send(player);
        }
    }
}
