package lol.vedant.neptunecore.database;

import com.velocitypowered.api.proxy.player.PlayerSettings;
import lol.vedant.core.data.Friend;
import lol.vedant.core.data.FriendRequest;
import lol.vedant.core.data.Setting;
import lol.vedant.core.data.UserSettings;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public interface Database {

    /*
    For friends methods
    PLAYER = player executing the command
    TARGET = player (friend)
     */

    void init();
    void close();

    boolean exists(UUID player);
    void createPlayer(String username, UUID uuid);

    UserSettings getSettings(int playerId);
    void saveSetting(int id, Setting type, String value);

    int getPlayerId(UUID uuid);
    int getPlayerId(String username);

    String getNameById(int playerId);
    String getNameByUUID(UUID uuid);

    List<Friend> getFriends(int playerId);
    void removeFriend(int playerId,int targetId);
    void addFriend(int playerId, int targetId);

    List<FriendRequest> getFriendRequests(int playerId);
    void createFriendRequest(int sender, int receiver);
    void removeFriendRequest(int sender, int receiver);


}