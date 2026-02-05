package lol.vedant.neptunecore.data;


import com.velocitypowered.api.proxy.Player;
import lol.vedant.core.data.PlayerData;
import lol.vedant.neptunecore.NeptuneCore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cache {

    NeptuneCore plugin = NeptuneCore.getInstance();
    Map<UUID, PlayerData> playerData = new HashMap<>();


    public void loadData(Player player) {
        PlayerData data = new PlayerData();
        data.setName(player.getUsername());
        data.setUuid(player.getUniqueId());
        data.setPlayerId(plugin.getDatabase().getPlayerId(player.getUniqueId()));
        data.setFriends(plugin.getDatabase().getFriends(data.getPlayerId()));
        data.setRequests(plugin.getDatabase().getFriendRequests(data.getPlayerId()));
        data.setSettings(plugin.getDatabase().getSettings(data.getPlayerId()).getAllSettings());
        playerData.put(player.getUniqueId(), data);
    }

    public void remove(Player player) {
        playerData.remove(player.getUniqueId());
    }

    public PlayerData getPlayerData(UUID uuid) {
        return playerData.get(uuid);
    }

    public void reloadFriends(Player player) {
        PlayerData data = playerData.get(player.getUniqueId());
        data.setFriends(plugin.getDatabase().getFriends(data.getPlayerId()));
        data.setRequests(plugin.getDatabase().getFriendRequests(data.getPlayerId()));
        playerData.put(player.getUniqueId(), data);
    }
}