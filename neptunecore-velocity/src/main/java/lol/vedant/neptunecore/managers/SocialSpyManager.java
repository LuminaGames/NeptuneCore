package lol.vedant.neptunecore.managers;

import com.velocitypowered.api.proxy.Player;

import java.util.ArrayList;
import java.util.List;

public class SocialSpyManager {


    private static List<Player> socialSpyToggled = new ArrayList<>();

    public static void toggle(Player player) {
        if(socialSpyToggled.contains(player)) {
            socialSpyToggled.remove(player);
        } else {
            socialSpyToggled.add(player);
        }
    }

    public static List<Player> getToggledPlayers() {
        return socialSpyToggled;
    }

    public static boolean isEnabled(Player player) {
        return socialSpyToggled.contains(player);
    }

}
