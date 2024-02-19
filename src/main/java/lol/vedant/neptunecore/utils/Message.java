package lol.vedant.neptunecore.utils;

import net.md_5.bungee.config.Configuration;

public enum Message {

    PREFIX("GENERAL.PREFIX"),
    STAFF_CHAT_PREFIX("STAFF_CHAT.PREFIX"),
    STAFF_CHAT_FORMAT("STAFF_CHAT.FORMAT");

    private static net.md_5.bungee.config.Configuration config;
    private final String path;

    Message(String path) {
        this.path = path;
    }

    public static void setConfiguration(Configuration c) {
        config = c;
    }

    private String replace(String message, Object... replacements) {
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 >= replacements.length) break;
            message = message.replace(String.valueOf(replacements[i]), String.valueOf(replacements[i + 1]));
        }

        String prefix = config.getString(PREFIX.getPath());
        String sc_prefix = config.getString(STAFF_CHAT_PREFIX.getPath());
        return message
                .replace("{prefix}", prefix != null && !prefix.isEmpty() ? prefix : "")
                .replace("{sc_prefix}", sc_prefix != null && !sc_prefix.isEmpty() ? sc_prefix : "");
    }

    public String getPath() {
        return this.path;
    }

    public String asString() {
        return Utils.cc(replace(config.getString(this.path)));
    }
}
