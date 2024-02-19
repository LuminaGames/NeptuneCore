package lol.vedant.neptunecore.managers;


import lol.vedant.neptunecore.utils.Message;
import lol.vedant.neptunecore.utils.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;


public class AdminChatManager {

    private static List<ProxiedPlayer> adminChatToggled = new ArrayList<>();

    public static boolean isEnabled(ProxiedPlayer player) {
        return adminChatToggled.contains(player);
    }

    public static void sendAdminChat(ProxiedPlayer player, String message) {
        for (ProxiedPlayer staff : ProxyServer.getInstance().getPlayers()) {
            if(staff.hasPermission("neptune.adminchat.receive")) {
                staff.sendMessage(new TextComponent(
                        Message.ADMIN_CHAT_FORMAT.asString()
                                .replace("{player}", player.getName())
                                .replace("{message}", Utils.cc(message)
                                )));
            }
        }
    }

    public static void toggleAdminChat(ProxiedPlayer player) {
        if(isEnabled(player)) {
            adminChatToggled.remove(player);
        } else {
            adminChatToggled.add(player);
        }
    }





}
