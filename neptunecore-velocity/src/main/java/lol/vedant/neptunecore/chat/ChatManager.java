package lol.vedant.neptunecore.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatManager {


    private final Map<UUID, Chat> playerChatModes = new HashMap<>();

    public Chat getChatChannel(UUID playerId) {
        return playerChatModes.getOrDefault(playerId, Chat.PUBLIC_CHAT);
    }

    public void setChatChannel(UUID playerId, Chat channel) {
        playerChatModes.put(playerId, channel);
    }

}
