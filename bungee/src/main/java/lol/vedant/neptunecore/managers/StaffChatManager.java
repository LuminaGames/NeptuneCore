package lol.vedant.neptunecore.managers;

import lol.vedant.neptunecore.utils.Message;
import lol.vedant.neptunecore.utils.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class StaffChatManager {

    private static List<ProxiedPlayer> staffChatToggled = new ArrayList<>();

    public static boolean isEnabled(ProxiedPlayer player) {
        return staffChatToggled.contains(player);
    }

    public static void sendStaffChat(ProxiedPlayer player, String message) {
        for (ProxiedPlayer staff : ProxyServer.getInstance().getPlayers()) {
            if(staff.hasPermission("neptune.staffchat.receive")) {
                staff.sendMessage(new TextComponent(
                        Message.STAFF_CHAT_FORMAT.asString()
                                .replace("{player}", player.getName())
                                .replace("{message}", Utils.cc(message)
                                )));
            }
        }
    }

    public static void toggleStaffChat(ProxiedPlayer player) {
        if(isEnabled(player)) {
            staffChatToggled.remove(player);
            player.sendMessage(new TextComponent(Utils.cc(
                    Message.STAFF_CHAT_OFF.asString()
            )));
        } else {
            staffChatToggled.add(player);
            player.sendMessage(new TextComponent(Utils.cc(
                    Message.STAFF_CHAT_ON.asString()
            )));
        }
    }





}
