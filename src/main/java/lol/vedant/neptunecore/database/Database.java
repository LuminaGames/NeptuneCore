package lol.vedant.neptunecore.database;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public interface Database {

    void init();

    void addFriend(ProxiedPlayer adder, ProxiedPlayer friend);

    void removeFriend();

}
