package lol.vedant.neptunecore.managers;

import lol.vedant.neptunecore.utils.Message;
import lol.vedant.neptunecore.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;

public class MessageManager {

    private static final Map<ProxiedPlayer, ProxiedPlayer> lastMessage = new HashMap<>();

    public static void message(ProxiedPlayer player, ProxiedPlayer receiver, String message) {
        receiver.sendMessage(new TextComponent(
                Utils.cc(Message.MESSAGE_FORMAT.asString()
                        .replace("{player}", player.getName())
                        .replace("{message}", message)
                )
        ));
        setLastMessage(receiver, player);
        setLastMessage(player, receiver);
    }

    public static void setLastMessage(ProxiedPlayer player, ProxiedPlayer sender) {
        lastMessage.put(player, sender);
    }

    public static ProxiedPlayer getLastMessage(ProxiedPlayer player) {
        return lastMessage.get(player);
    }

}
