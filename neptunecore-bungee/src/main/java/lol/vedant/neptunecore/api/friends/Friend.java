package lol.vedant.neptunecore.api.friends;


import java.sql.Timestamp;

public class Friend {

    private final String name;
    private final Timestamp friendSince;

    public Friend(String name, Timestamp friendSince) {
        this.name = name;
        this.friendSince = friendSince;
    }

    public String getName() {
        return name;
    }

    public Timestamp getFriendSince() {
        return friendSince;
    }
}
