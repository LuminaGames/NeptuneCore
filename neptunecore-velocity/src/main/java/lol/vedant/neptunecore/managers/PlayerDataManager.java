package lol.vedant.neptunecore.managers;

import com.velocitypowered.api.proxy.Player;
import lol.vedant.core.data.PlayerData;
import lol.vedant.neptunecore.NeptuneCore;


import java.util.HashMap;
import java.util.Map;

public class PlayerDataManager {
    static NeptuneCore plugin = NeptuneCore.getInstance();

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

    public static void savePlayerData(Player player) {
        plugin.getDatabase().saveFriends(playerData.get(player).getFriends(), player.getUsername());
        plugin.getDatabase().saveUserSettings(playerData.get(player).getSettings(), player.getUniqueId());
    }

}
