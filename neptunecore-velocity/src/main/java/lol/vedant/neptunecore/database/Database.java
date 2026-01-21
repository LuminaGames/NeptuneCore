package lol.vedant.neptunecore.database;

import com.velocitypowered.api.proxy.Player;
import lol.vedant.core.data.Friend;
import lol.vedant.core.data.FriendRequest;
import lol.vedant.core.data.UserSettings;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Database {

    void init();

    void createPlayer(UUID uuid, String name);

    Map<String, Object> getPlayerSettings(int playerId);

    void updatePlayerSettings(int playerId, Map<String, Object> settings);

    boolean addFriendship(UUID player1, UUID player2);

    boolean removeFriendship(UUID player1, UUID player2);

    boolean createFriendRequest(UUID requester, UUID receiver);

    List<FriendRequest> getFriendRequests(UUID receiverUuid);

    List<Friend> getFriends(UUID uuid);

    int getPlayerId(UUID uuid);
}
