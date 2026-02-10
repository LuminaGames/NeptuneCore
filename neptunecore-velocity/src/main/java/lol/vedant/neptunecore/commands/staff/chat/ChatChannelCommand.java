package lol.vedant.neptunecore.commands.staff.chat;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.core.utils.CommonUtil;
import lol.vedant.neptunecore.module.chat.Chat;
import lol.vedant.neptunecore.module.chat.ChatManager;
import lol.vedant.neptunecore.utils.Message;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ChatChannelCommand implements SimpleCommand {

    private final ConcurrentHashMap<Player, Chat> playerChatChannels = new ConcurrentHashMap<>();

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            Message.PLAYER_ONLY_COMMAND.send(invocation.source());
            return;
        }

        String[] args = invocation.arguments();
        if (args.length < 1) {
            player.sendMessage(CommonUtil.mm("<yellow>Usage: /chat <normal|staff|admin></yellow>"));
            return;
        }

        String channelInput = args[0].toLowerCase();
        Chat chatChannel;
        switch (channelInput) {
            case "normal" -> {
                chatChannel = Chat.PUBLIC_CHAT;
                player.sendMessage(CommonUtil.mm("<green>Switched to PUBLIC channel"));
            }
            case "staff" -> {
                if (!player.hasPermission("neptune.staff")) {
                    Message.NO_PERMISSION.send(player);
                    return;
                }
                chatChannel = Chat.STAFF_CHAT;
                Message.STAFF_CHAT_ON.send(player);
            }
            case "admin" -> {
                if (!player.hasPermission("neptune.admin")) {
                    Message.NO_PERMISSION.send(player);
                    return;
                }
                chatChannel = Chat.ADMIN_CHAT;
                Message.ADMIN_CHAT_ON.send(player);
            }
            default -> {
                player.sendMessage(CommonUtil.mm("<red>Invalid chat channel. Use: <yellow>normal, staff, admin</yellow></red>"));
                return;
            }
        }

        ChatManager.setChatChannel(player.getUniqueId(), chatChannel);
        player.sendMessage(CommonUtil.mm("<green>You have switched to the <yellow>" + chatChannel.name() + "</yellow> channel.</green>"));
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        if (invocation.source() instanceof Player player) {
            List<String> suggestions = Arrays.asList("normal", "staff", "admin");
            if (!player.hasPermission("neptune.staff")) {
                suggestions = suggestions.stream().filter(s -> !s.equals("staff")).toList();
            }
            if (!player.hasPermission("neptune.admin")) {
                suggestions = suggestions.stream().filter(s -> !s.equals("admin")).toList();
            }
            return CompletableFuture.completedFuture(suggestions);
        }
        return CompletableFuture.completedFuture(List.of());
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("neptune.staff") || invocation.source().hasPermission("neptune.admin");
    }

}
