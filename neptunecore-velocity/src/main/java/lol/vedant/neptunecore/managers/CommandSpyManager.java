package lol.vedant.neptunecore.managers;

import com.velocitypowered.api.proxy.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandSpyManager {

    private static List<Player> commandSpyToggled = new ArrayList<>();

    public static void toggle(Player player) {
        if(commandSpyToggled.contains(player)) {
            commandSpyToggled.remove(player);
        } else {
            commandSpyToggled.add(player);
        }
    }

    public static List<Player> getToggledPlayers() {
        return commandSpyToggled;
    }

    public static boolean isEnabled(Player player) {
        return commandSpyToggled.contains(player);
    }
}
