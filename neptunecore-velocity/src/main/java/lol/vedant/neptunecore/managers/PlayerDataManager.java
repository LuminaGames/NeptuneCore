package lol.vedant.neptunecore.managers;

import com.velocitypowered.api.proxy.Player;
import lol.vedant.core.data.PlayerData;

import java.util.HashMap;
import java.util.Map;

public class PlayerDataManager {

    public static Map<Player, PlayerData> playerData = new HashMap<>();

    public static PlayerData getPlayerData(Player player) {
        return playerData.get(player);
    }

    public static void setPlayerData(Player player, PlayerData data) {
        playerData.put(player, data);
    }

    public static void removeData(Player player) {
        playerData.remove(player);
    }

}
