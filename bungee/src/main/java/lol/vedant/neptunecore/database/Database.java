package lol.vedant.neptunecore.database;

import lol.vedant.neptunecore.api.friends.Friend;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public interface Database {

    void init();

    void addFriend(String adder, String friend);

    void removeFriend(String player, String removedFriend);

    void sendFriendRequest(String player, String friend);

    List<Friend> getPendingRequests(String player);

    boolean areFriends(String player, String friend);

}
