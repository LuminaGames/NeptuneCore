package lol.vedant.neptunecore.module.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatManager {


    public static Map<UUID, Chat> playerChatModes = new HashMap<>();

    public static Chat getChatChannel(UUID playerId) {
        return playerChatModes.getOrDefault(playerId, Chat.PUBLIC_CHAT);
    }

    public static void setChatChannel(UUID playerId, Chat channel) {
        playerChatModes.put(playerId, channel);
    }

}
