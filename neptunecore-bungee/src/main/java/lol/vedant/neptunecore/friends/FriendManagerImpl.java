package lol.vedant.neptunecore.friends;

import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.api.friends.Friend;
import lol.vedant.neptunecore.api.friends.FriendManager;
import lol.vedant.neptunecore.utils.Message;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class FriendManagerImpl implements FriendManager {

    private NeptuneCore plugin;

    public FriendManagerImpl(NeptuneCore plugin) {
        this.plugin = plugin;
    }


    @Override
    public void addFriend(String player, String friend) {
        plugin.getDatabase().addFriend(player, friend);
        plugin.getDatabase().denyFriendRequest(player, friend);
        plugin.getDatabase().denyFriendRequest(friend, player);
        ProxiedPlayer player_ = ProxyServer.getInstance().getPlayer(player);
        if(player_ != null) {
            Message.FRIEND_REQUEST_SENT.send(player_, "{player}", friend);
        }
        ProxiedPlayer friend_ = ProxyServer.getInstance().getPlayer(friend);

        if(friend_ != null) {
           Message.FRIEND_REQUEST.send(friend_, "{player}", player);
        }
    }

    @Override
    public void removeFriend(String player, String friend) {
        plugin.getDatabase().removeFriend(player, friend);
        ProxiedPlayer player_ = ProxyServer.getInstance().getPlayer(player);
        if(player_ != null) {
            //Friend Remove message
        }
    }


    @Override
    public void sendFriendRequest(String player, String friend) {
        plugin.getDatabase().sendFriendRequest(player, friend);
        ProxiedPlayer player_ = ProxyServer.getInstance().getPlayer(player);
        if(player_ != null) {
            Message.FRIEND_REQUEST_SENT.send(player_, "{player}", friend);
        }

        ProxiedPlayer friend_ = ProxyServer.getInstance().getPlayer(friend);
        if(friend_.isConnected()) {
            Message.FRIEND_REQUEST.send(friend_, "{player}", player);
        }

    }

    @Override
    public void sendFriendMessage(String player, String friend, String message) {
        ProxiedPlayer player_ = ProxyServer.getInstance().getPlayer(player);
        ProxiedPlayer friend_ = ProxyServer.getInstance().getPlayer(friend);

        player_.sendMessage(new TextComponent(Message.MESSAGE_FORMAT.asString()
                .replace("{player}", player_.getName())
                .replace("{message}", message)));
        friend_.sendMessage(new TextComponent(Message.MESSAGE_FORMAT.asString()
                .replace("{player}", player_.getName())
                .replace("{message}", message)));
    }

    @Override
    public List<Friend> getPendingRequests(String player) {
        return plugin.getDatabase().getPendingRequests(player);
    }

    @Override
    public List<Friend> getFriends(String player) {
        return plugin.getDatabase().getFriends(player);
    }

    @Override
    public void denyFriendRequest(String player, String sender) {
        plugin.getDatabase().denyFriendRequest(player, sender);

        ProxiedPlayer player_ = ProxyServer.getInstance().getPlayer(player);
        if(player_ != null) {
            Message.FRIEND_DENY.send(player_, "{player}", sender);
        }
    }

    @Override
    public boolean areFriends(String player, String friend) {
        return plugin.getDatabase().areFriends(player, friend);
    }
}
