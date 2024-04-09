package lol.vedant.neptunecore.api.friends;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public interface FriendManager {

    void addFriend(String player, String friend);

    void removeFriend(String player, String friend);

    void sendFriendRequest(String player, String friend);

    void sendFriendMessage(String player, String friend);

    List<Friend> getPendingRequests(String player);

    boolean areFriends(String player, String friend);


}
