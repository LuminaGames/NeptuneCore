package lol.vedant.neptunecore.event;

import com.velocitypowered.api.proxy.Player;

public class PlayerMessageEvent {

    private final Player sender;
    private final Player receiver;
    private final String message;

    public PlayerMessageEvent(Player sender, Player receiver, String message) {
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
