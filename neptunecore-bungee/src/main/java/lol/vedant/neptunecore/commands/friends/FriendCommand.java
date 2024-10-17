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
                Message.NO_PLAYER_SPECIFIED.send(sender);
                return;
            }

            if(args[1].equalsIgnoreCase(sender.getName())) {
                Message.FRIEND_REQUEST_SELF.send(sender);
                return;
            }
            String friend = args[1];

            if(manager.areFriends(friend, sender.getName())) {
                Message.ALREADY_FRIENDS.send(sender);
                return;
            }

            manager.sendFriendRequest(sender.getName(), friend);

        } else if(args[0].equalsIgnoreCase("accept")) {
            if(args.length < 2) {
                Message.NO_PLAYER_SPECIFIED.send(sender);
                return;
            }

            if(args[1].equalsIgnoreCase(sender.getName())) {
                Message.FRIEND_REQUEST_SELF.send(sender);
                return;
            }
            String friend = args[1];

            if(manager.areFriends(friend, sender.getName())) {
                Message.ALREADY_FRIENDS.send(sender);
                return;
            }

            List<Friend> pendingRequests = manager.getPendingRequests(sender.getName());

            for (Friend f : pendingRequests) {
                if(f.getName().equalsIgnoreCase(friend)) {
                    manager.addFriend(sender.getName(), f.getName());
                    sender.sendMessage(new TextComponent("You added " + f.getName()));
                } else {
                    Message.FRIEND_REQUEST_NOT_FOUND.send(sender);
                }
            }
        } else if (args[0].equalsIgnoreCase("remove")) {

            if(args.length < 2) {
                Message.NO_PLAYER_SPECIFIED.send(sender);
                return;
            }

            String friend = args[1];

            if(!manager.areFriends(friend, sender.getName())) {
                Message.NOT_FRIENDS.send(sender);
                return;
            }

            manager.removeFriend(sender.getName(), friend);
        } else if(args[0].equalsIgnoreCase("requests")) {

            List<Friend> pendingRequests = manager.getPendingRequests(sender.getName());

            if(pendingRequests == null) {
                // No friend requests
                return;
            }

            sender.sendMessage(new TextComponent(Utils.cc("&bYour current pending requests")));
            for (Friend f : pendingRequests) {
                sender.sendMessage(new TextComponent(Utils.cc("&b&l" + f.getName() + "&7- " + f.getFriendSince())));
            }

        } else if(args[0].equalsIgnoreCase("deny")) {
            List<Friend> pendingRequests = manager.getPendingRequests(sender.getName());

            if(args.length < 2) {
                Message.NO_PLAYER_SPECIFIED.send(sender);
                return;
            }

            String requestSender = args[1];

            for (Friend f : pendingRequests) {
                if(f.getName().equalsIgnoreCase(requestSender)) {
                    manager.denyFriendRequest(sender.getName(), requestSender);
                    Message.FRIEND_DENY.send(sender, "{player}", requestSender);
                } else {
                    Message.FRIEND_REQUEST_NOT_FOUND.send(sender);
                }
            }
        } else if(args[0].equalsIgnoreCase("list")) {
            List<Friend> friends = manager.getFriends(sender.getName());
            Message.FRIEND_LIST_MESSAGE.send(sender);

            if(friends == null) {
                Message.NO_FRIENDS.send(sender);
                return;
            }

            for (Friend f : friends) {
                sender.sendMessage(new TextComponent(Utils.cc("&b&l" + f.getName() + "&7- " + f.getFriendSince())));
            }
        }
    }
}
