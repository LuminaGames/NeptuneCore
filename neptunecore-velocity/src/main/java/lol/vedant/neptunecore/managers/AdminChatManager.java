package lol.vedant.neptunecore.managers;

import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.utils.Message;

import java.util.ArrayList;
import java.util.List;

import static lol.vedant.neptunecore.NeptuneCore.server;

public class AdminChatManager {

    private static List<Player> adminChatToggled = new ArrayList<>();

    public static boolean isEnabled(Player player) {
        return adminChatToggled.contains(player);
    }

    public static void sendAdminChat(Player player, String message) {
        for (Player staff : server.getAllPlayers()) {
            if(staff.hasPermission("neptune.adminchat.receive")) {
                Message.ADMIN_CHAT_FORMAT.send(player,
                        "{player}", player.getUsername(),
                        "{message}", message);
            }
        }
    }

    public static void toggleAdminChat(Player player) {
        if(isEnabled(player)) {
            adminChatToggled.remove(player);
            Message.ADMIN_CHAT_OFF.send(player);
        } else {
            adminChatToggled.add(player);
            Message.ADMIN_CHAT_ON.send(player);
        }
    }





}
