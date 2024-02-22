package lol.vedant.neptunecore.api.events;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

public class PrivateMessageEvent extends Event {

    private final ProxiedPlayer sender;
    private final ProxiedPlayer receiver;
    private final String message;

    public PrivateMessageEvent(ProxiedPlayer sender, ProxiedPlayer receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public ProxiedPlayer getSender() {
        return sender;
    }

    public ProxiedPlayer getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }
}
