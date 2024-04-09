package lol.vedant.neptunecore.database;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public interface Database {

    void init();

    void addFriend(String adder, String friend);

    void removeFriend(String player, String removedFriend);

    void sendFriendRequest(String player, String friend);

}
