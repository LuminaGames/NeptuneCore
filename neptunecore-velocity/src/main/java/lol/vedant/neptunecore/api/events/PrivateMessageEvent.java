package lol.vedant.neptunecore.api.events;

import com.velocitypowered.api.proxy.Player;

public class PrivateMessageEvent {

    private final Player sender;
    private final Player receiver;
    private final String message;

    public PrivateMessageEvent(Player sender, Player receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public Player getSender() {
        return sender;
    }

    public Player getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }
}
