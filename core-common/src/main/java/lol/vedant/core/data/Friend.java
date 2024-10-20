package lol.vedant.core.data;

import java.time.Instant;
import java.util.UUID;

public class Friend {

    String username;
    UUID uuid;
    Instant friendSince;

    public Friend(String username, UUID uuid, Instant friendSince) {
        this.username = username;
        this.uuid = uuid;
        this.friendSince = friendSince;
    }

    public String getUsername() {
        return username;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Instant getFriendSince() {
        return friendSince;
    }
}
