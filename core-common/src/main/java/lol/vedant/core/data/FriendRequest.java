package lol.vedant.core.data;

public class FriendRequest {

    String sender;
    String player;

    public FriendRequest(String sender, String player) {
        this.sender = sender;
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    public String getSender() {
        return sender;
    }
}
