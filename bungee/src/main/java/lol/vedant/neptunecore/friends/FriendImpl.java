package lol.vedant.neptunecore.friends;

import lol.vedant.neptunecore.api.friends.Friend;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FriendImpl implements Friend {

    private String name;
    private ProxiedPlayer player;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ProxiedPlayer getPlayer() {
        return this.player;
    }
}
