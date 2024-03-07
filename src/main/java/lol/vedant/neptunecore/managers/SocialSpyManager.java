package lol.vedant.neptunecore.managers;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class SocialSpyManager {

    private static List<ProxiedPlayer> socialSpyToggled = new ArrayList<>();

    public static void toggle(ProxiedPlayer player) {
        if(socialSpyToggled.contains(player)) {
            socialSpyToggled.remove(player);
        } else {
            socialSpyToggled.add(player);
        }
    }

    public static boolean isEnabled(ProxiedPlayer player) {
        return socialSpyToggled.contains(player);
    }

}
