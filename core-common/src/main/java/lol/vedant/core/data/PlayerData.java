package lol.vedant.core.data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    private int playerId;
    private String name;
    private UUID uuid;
    private Map<Setting, String> settings;
    private List<Friend> friends;
    private List<FriendRequest> requests;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Map<Setting, String> getSettings() {
        return settings;
    }

    public void setSettings(Map<Setting, String> settings) {
        this.settings = settings;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public void setRequests(List<FriendRequest> requests) {
        this.requests = requests;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public List<FriendRequest> getRequests() {
        return requests;
    }
}
