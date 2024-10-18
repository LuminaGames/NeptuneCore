package lol.vedant.neptunecore.managers;

import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.api.events.PrivateMessageEvent;
import lol.vedant.neptunecore.utils.Message;

import java.util.HashMap;
import java.util.Map;

import static lol.vedant.neptunecore.NeptuneCore.server;

public class MessageManager {

    private static final Map<Player, Player> lastMessage = new HashMap<>();

    public static void message(Player player, Player receiver, String message) {
        Message.MESSAGE_FORMAT.send(receiver, "{player}", player.getUsername(), "{message}", message);
        Message.MESSAGE_FORMAT.send(player, "{player}", player.getUsername(), "{message}", message);
        setLastMessage(receiver, player);
        setLastMessage(player, receiver);

        server.getEventManager().fire(new PrivateMessageEvent(player, receiver, message));
    }

    public static void setLastMessage(Player player, Player sender) {
        lastMessage.put(player, sender);
    }

    public static Player getLastMessage(Player player) {
        return lastMessage.get(player);
    }

    public static boolean hasLastMessage(Player player) {
        return lastMessage.containsKey(player);
    }

}
