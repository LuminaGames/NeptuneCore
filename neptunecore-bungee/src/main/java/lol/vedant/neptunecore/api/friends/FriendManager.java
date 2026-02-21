package lol.vedant.neptunecore.api.friends;


import java.util.List;

public interface FriendManager {

    void addFriend(String player, String friend);

    void removeFriend(String player, String friend);

    void sendFriendRequest(String player, String friend);

    void sendFriendMessage(String player, String friend, String message);

    List<Friend> getPendingRequests(String player);

    List<Friend> getFriends(String player);

    void denyFriendRequest(String player, String sender);

    boolean areFriends(String player, String friend);


}
