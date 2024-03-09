package lol.vedant.neptunecore.utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;

import java.util.List;

public enum Message {

    //General stuff
    PREFIX("GENERAL.PREFIX"),
    PLAYER_ONLY_COMMAND("GENERAL.PLAYER_ONLY_COMMAND"),
    HELP_1("GENERAL.HELP_1"),
    STAFF_ONLINE("GENERAL.ONLINE_STAFF"),

    //Staff chat
    STAFF_CHAT_PREFIX("STAFF_CHAT.PREFIX"),
    STAFF_CHAT_FORMAT("STAFF_CHAT.FORMAT"),
    STAFF_CHAT_ON("STAFF_CHAT.TOGGLE_ON"),
    STAFF_CHAT_OFF("STAFF_CHAT.TOGGLE_OFF"),

    //Admin chat
    ADMIN_CHAT_ON("ADMIN_CHAT.TOGGLE_ON"),
    ADMIN_CHAT_OFF("ADMIN_CHAT.TOGGLE_OFF"),
    ADMIN_CHAT_FORMAT("ADMIN_CHAT.FORMAT"),
    ADMIN_CHAT_PREFIX("ADMIN_CHAT.PREFIX"),

    //Private messaging
    MESSAGE_PREFIX("PRIVATE_MESSAGE.PREFIX"),
    MESSAGE_FORMAT("PRIVATE_MESSAGE.FORMAT"),
    OFFLINE_PLAYER("PRIVATE_MESSAGE.OFFLINE_PLAYER"),
    NO_ONE_REPLY("PRIVATE_MESSAGE.NO_ONE_REPLY"),

    //Staff Messages
    STAFF_PREFIX("STAFF.PREFIX"),
    SERVER_SWITCH("STAFF.SERVER_SWITCH"),
    SERVER_JOIN("STAFF.SERVER_JOIN"),
    SOCIAL_SPY("STAFF.SOCIAL_SPY");



    private static Configuration config;
    private final String path;

    Message(String path) {
        this.path = path;
    }

    public static void setConfiguration(Configuration c) {
        config = c;
    }

    public void send(CommandSender receiver, Object... replacements) {
        Object value = config.get(this.path);

        String message;
        if (value == null) {
            message = "Neptune: message not found (" + this.path + ")";
        } else {
            message = value instanceof List ? Utils.fromList((List<?>) value) : value.toString();
        }

        if (!message.isEmpty()) {
            receiver.sendMessage(new TextComponent(Utils.cc(replace(message, replacements))));
        }
    }

    private String replace(String message, Object... replacements) {
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 >= replacements.length) break;
            message = message.replace(String.valueOf(replacements[i]), String.valueOf(replacements[i + 1]));
        }

        String prefix = config.getString(PREFIX.getPath());
        String sc_prefix = config.getString(STAFF_CHAT_PREFIX.getPath());
        String ac_prefix = config.getString(ADMIN_CHAT_PREFIX.getPath());
        String msg_prefix = config.getString(MESSAGE_PREFIX.getPath());
        String s_prefix = config.getString(STAFF_PREFIX.getPath());

        return message
                .replace("{prefix}", prefix != null && !prefix.isEmpty() ? prefix : "")
                .replace("{sc_prefix}", sc_prefix != null && !sc_prefix.isEmpty() ? sc_prefix : "")
                .replace("{msg_prefix}", msg_prefix != null && !msg_prefix.isEmpty() ? msg_prefix : "")
                .replace("{ac_prefix}", ac_prefix != null && !ac_prefix.isEmpty() ? ac_prefix : "")
                .replace("{sc_prefix}", s_prefix != null && !s_prefix.isEmpty() ? s_prefix : "");
    }

    public String getPath() {
        return this.path;
    }

    public String asString() {
        return Utils.cc(replace(config.getString(this.path)));
    }
}
