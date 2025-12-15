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
        data.setFriends(db.getFriends(player.getUniqueId()));
        data.setSettings(db.getPlayerSettings(data.getPlayerId()));
        playerData.put(player.getUniqueId(), data);
    }

    public void clearData(UUID uuid) {
        playerData.remove(uuid);
    }

    public PlayerData getData(UUID player) {
        return playerData.get(player);
    }




}
