package lol.vedant.neptunecore.data;

import com.velocitypowered.api.proxy.Player;
import lol.vedant.core.data.PlayerData;
import lol.vedant.neptunecore.database.Database;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cache {

    private Database db;

    public Cache(Database db) {
        this.db = db;
    }

    public Map<UUID, PlayerData> playerData = new HashMap<>();

    public void loadPlayerData(Player player) {
        PlayerData data = new PlayerData();
        data.setName(player.getUsername());
        data.setUuid(player.getUniqueId());
        data.setPlayerId(db.getPlayerId(player.getUniqueId()));
        data.setFriends(db.getFriends(data.getPlayerId()));
        data.setSettings(db.getPlayerSettings(data.getPlayerId()));
        playerData.put(player.getUniqueId(), data);
    }

    public void clearData(UUID uuid) {
        playerData.remove(uuid);
    }

    public void reloadFriends(Player player) {
        PlayerData data = playerData.get(player.getUniqueId());
        data.setFriends(db.getFriends(playerData.get(player.getUniqueId()).getPlayerId()));
        playerData.put(player.getUniqueId(), data);
    }

    public PlayerData getData(UUID player) {
        return playerData.get(player);
    }




}
