package lol.vedant.neptunecore.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.chat.Chat;
import lol.vedant.neptunecore.chat.ChatManager;
import net.kyori.adventure.text.Component;

public class PlayerChatListener {

    private final ChatManager chatManager;

    public PlayerChatListener(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent e) {
        Player player = e.getPlayer();
        Chat channel = chatManager.getChatChannel(player.getUniqueId());

        e.setResult(PlayerChatEvent.ChatResult.denied());

        switch (channel) {
            case STAFF_CHAT -> {
                broadcastToGroup(player, e.getMessage(), "neptune.command.staffchat");
            }
            case ADMIN_CHAT -> {
                broadcastToGroup(player, e.getMessage(), "neptune.command.adminchat");
            }
            case PUBLIC_CHAT -> {
                broadcastToAll(player, e.getMessage());
            }
        }
    }

    private void broadcastToGroup(Player sender, String message, String permission) {
        sender.getCurrentServer().ifPresent(server -> {
            server.getServer().getPlayersConnected().stream()
                    .filter(player -> player.hasPermission(permission))
                    .forEach(player -> player.sendMessage(Component.text()));
        });
    }

    private void broadcastToAll(Player sender, String message) {
        sender.getCurrentServer().ifPresent(server -> {
            server.getServer().sendMessage(Component.text(sender.getUsername() + ": " + message));
        });
    }

}
