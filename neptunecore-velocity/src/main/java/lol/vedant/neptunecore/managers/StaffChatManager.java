package lol.vedant.neptunecore.managers;

import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.utils.Message;

import java.util.ArrayList;
import java.util.List;

import static lol.vedant.neptunecore.NeptuneCore.server;

public class StaffChatManager {

    private static List<Player> staffChatToggled = new ArrayList<>();

    public static boolean isEnabled(Player player) {
        return staffChatToggled.contains(player);
    }

    public static void sendStaffChat(Player player, String message) {
        for (Player staff : server.getAllPlayers()) {
            if(staff.hasPermission("neptune.staffchat.receive")) {
                Message.STAFF_CHAT_FORMAT.send(player,
                        "{player}", player.getUsername(),
                        "{message}", message);
            }
        }
    }

    public static void toggleStaffChat(Player player) {
        if(isEnabled(player)) {
            staffChatToggled.remove(player);
            Message.STAFF_CHAT_OFF.send(player);
        } else {
            staffChatToggled.add(player);
            Message.STAFF_CHAT_ON.send(player);
        }
    }





}
