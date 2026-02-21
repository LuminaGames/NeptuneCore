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
    NO_PLAYER_SPECIFIED("GENERAL.NO_PLAYER_SPECIFIED"),

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
    SOCIAL_SPY("SOCIAL_SPY.FORMAT"),
    COMMAND_SPY("COMMAND_SPY.FORMAT"),

    //Friend Messages
    FRIEND_PREFIX("FRIEND.PREFIX"),
    FRIEND_DONE("FRIEND.FRIEND_DONE"),
    FRIEND_ACCEPT("FRIEND.FRIEND_ACCEPT"),
    FRIEND_DENY("FRIEND.FRIEND_DENY"),
    FRIEND_PENDING_REQUESTS("FRIEND.PENDING_REQUESTS"),
    FRIEND_REQUEST_SELF("FRIEND.REQUEST_SELF"),
    FRIEND_REQUEST_SENT("FRIEND.REQUEST_SENT"),
    FRIEND_REQUEST_NOT_FOUND("FRIEND.REQUEST_NOT_FOUND"),
    FRIEND_REQUEST("FRIEND.REQUEST"),
    FRIEND_HELP("FRIEND.HELP"),
    FRIEND_MESSAGE_FORMAT("FRIEND.MESSAGE_FORMAT"),
    FRIEND_LIST_MESSAGE("FRIEND.LIST_FORMAT"),
    FRIEND_OFFLINE("FRIEND.PLAYER_OFFLINE"),
    NOT_FRIENDS("FRIEND.NOT_FRIENDS"),
    ALREADY_FRIENDS("FRIEND.ALREADY_FRIENDS"),
    NO_FRIENDS("FRIEND.NO_FRIENDS");


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
        String f_prefix = config.getString(FRIEND_PREFIX.getPath());

        return message
                .replace("{prefix}", prefix != null && !prefix.isEmpty() ? prefix : "")
                .replace("{sc_prefix}", sc_prefix != null && !sc_prefix.isEmpty() ? sc_prefix : "")
                .replace("{msg_prefix}", msg_prefix != null && !msg_prefix.isEmpty() ? msg_prefix : "")
                .replace("{ac_prefix}", ac_prefix != null && !ac_prefix.isEmpty() ? ac_prefix : "")
                .replace("{s_prefix}", s_prefix != null && !s_prefix.isEmpty() ? s_prefix : "")
                .replace("{f_prefix}", f_prefix != null && !f_prefix.isEmpty() ? f_prefix : "");
    }

    public String getPath() {
        return this.path;
    }

    public String asString() {
        return Utils.cc(replace(config.getString(this.path)));
    }
}
