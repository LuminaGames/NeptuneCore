package lol.vedant.neptunecore.commands.friends;

import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.api.friends.Friend;
import lol.vedant.neptunecore.api.friends.FriendManager;
import lol.vedant.neptunecore.utils.Message;
import lol.vedant.neptunecore.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class FriendCommand extends Command {

    private NeptuneCore plugin;

    public FriendCommand(String name, String permission, NeptuneCore plugin, String... aliases) {
        super(name, permission, aliases);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        FriendManager manager = plugin.getFriendManager();

        if(args.length == 0) {
            Message.FRIEND_HELP.send(sender);
        } else if(args[0].equalsIgnoreCase("help")) {
            Message.FRIEND_HELP.send(sender);
        } else if (args[0].equalsIgnoreCase("add")) {

            if(args.length < 2) {
                sender.sendMessage(new TextComponent(Utils.cc("&cPlease specify a player to add as a friend.")));
                return;
            }

            if(args[1].equalsIgnoreCase(sender.getName())) {
                Message.FRIEND_REQUEST_SELF.send(sender);
                return;
            }
            String friend = args[1];

            if(manager.areFriends(friend, sender.getName())) {
                sender.sendMessage(new TextComponent(Utils.cc("&cYou are already friends with them.")));
                return;
            }

            manager.sendFriendRequest(sender.getName(), friend);

        } else if(args[0].equalsIgnoreCase("accept")) {
            if(args.length < 2) {
                sender.sendMessage(new TextComponent(Utils.cc("&cPlease specify a player.")));
                return;
            }

            if(args[1].equalsIgnoreCase(sender.getName())) {
                Message.FRIEND_REQUEST_SELF.send(sender);
                return;
            }
            String friend = args[1];

            if(manager.areFriends(friend, sender.getName())) {
                sender.sendMessage(new TextComponent(Utils.cc("&cYou are already friends with them.")));
                return;
            }

            List<Friend> pendingRequests = manager.getPendingRequests(sender.getName());

            for (Friend f : pendingRequests) {
                if(f.getName().equalsIgnoreCase(friend)) {
                    manager.addFriend(sender.getName(), f.getName());
                } else {
                    sender.sendMessage(new TextComponent(Utils.cc("&cYou don't have a pending request from them.")));
                }
            }
        } else if (args[0].equalsIgnoreCase("remove")) {

            if(args.length < 2) {
                sender.sendMessage(new TextComponent(Utils.cc("&cPlease specify a player.")));
                return;
            }

            String friend = args[1];

            if(!manager.areFriends(friend, sender.getName())) {
                sender.sendMessage(new TextComponent(Utils.cc("&cYou are not friends with them.")));
                return;
            }

            manager.removeFriend(sender.getName(), friend);
        } else if(args[0].equalsIgnoreCase("requests")) {

            List<Friend> pendingRequests = manager.getPendingRequests(sender.getName());

            sender.sendMessage(new TextComponent(Utils.cc("&bYour current pending requests")));
            for (Friend f : pendingRequests) {
                sender.sendMessage(new TextComponent(Utils.cc("&b&l" + f.getName() + "&7- " + f.getFriendSince())));
            }

        }
    }
}
