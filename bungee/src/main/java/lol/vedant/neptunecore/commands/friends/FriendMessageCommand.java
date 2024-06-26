package lol.vedant.neptunecore.commands.friends;

import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.utils.Message;
import lol.vedant.neptunecore.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;

public class FriendMessageCommand extends Command {


    private NeptuneCore plugin;

    public FriendMessageCommand(String name, String permission, NeptuneCore plugin, String... aliases) {
        super(name, permission, aliases);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(new TextComponent(Utils.cc("&c/fmsg <player> <message>")));
            return;
        }
        String friend = args[0];

        if(!plugin.getDatabase().areFriends(sender.getName(), friend)) {
            sender.sendMessage(new TextComponent(Utils.cc("&cYou are not friends with them.")));
            return;
        }

        ProxiedPlayer playerFriend = ProxyServer.getInstance().getPlayer(friend);

        if(playerFriend == null) {
            Message.FRIEND_OFFLINE.send(sender);
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        sender.sendMessage(new TextComponent(Utils.cc(Message.FRIEND_MESSAGE_FORMAT.asString())
                .replace("{player}", sender.getName())
                .replace("{message", message)
        ));

        playerFriend.sendMessage(new TextComponent(Utils.cc(Message.FRIEND_MESSAGE_FORMAT.asString())
                .replace("{player}", sender.getName())
                .replace("{message}", message)
        ));

    }

}
