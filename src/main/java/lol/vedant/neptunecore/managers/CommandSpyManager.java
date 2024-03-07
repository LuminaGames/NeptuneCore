package lol.vedant.neptunecore.managers;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class CommandSpyManager {

    private static List<ProxiedPlayer> commandSpyToggled = new ArrayList<>();

    public static void toggle(ProxiedPlayer player) {
        if(commandSpyToggled.contains(player)) {
            commandSpyToggled.remove(player);
        } else {
            commandSpyToggled.add(player);
        }
    }

    public static boolean isEnabled(ProxiedPlayer player) {
        return commandSpyToggled.contains(player);
    }

}
