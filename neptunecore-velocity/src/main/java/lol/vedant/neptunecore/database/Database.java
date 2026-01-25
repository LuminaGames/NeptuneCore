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

    int getPlayerId(UUID uuid);

    int getPlayerId(String username);

    Map<String, Object> getPlayerSettings(int playerId);

    void updatePlayerSettings(int playerId, Map<String, Object> settings);

    boolean addFriendship(int player1, int player2);

    boolean removeFriendship(int player1, int player2);

    boolean createFriendRequest(int requester, int receiver);

    void removeFriendRequest(int player1, int player2);

    List<FriendRequest> getFriendRequests(int receiverId);

    List<Friend> getFriends(int playerId);

}
