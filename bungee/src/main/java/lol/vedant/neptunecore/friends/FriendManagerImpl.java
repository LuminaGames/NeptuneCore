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
        ProxiedPlayer player_ = ProxyServer.getInstance().getPlayer(player);
        if(player_ != null) {

        }
    }

    @Override
    public void removeFriend(String player, String friend) {

    }

    @Override
    public void sendFriendRequest(String player, String friend) {

    }

    @Override
    public void sendFriendMessage(String player, String friend) {

    }

    @Override
    public List<Friend> getPendingRequests(String player) {
        return null;
    }

    @Override
    public boolean areFriends(String player, String friend) {
        return false;
    }
}
