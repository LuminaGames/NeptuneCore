package lol.vedant.neptunecore.commands.friend;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.core.data.FriendRequest;
import lol.vedant.core.data.PlayerData;
import lol.vedant.core.utils.CommonUtil;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.utils.Message;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FriendCommand implements SimpleCommand {

    private static NeptuneCore plugin = NeptuneCore.getInstance();

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            Message.PLAYER_ONLY_COMMAND.send(invocation.source());
            return;
        }

        String[] args = invocation.arguments();
        if (args.length == 0) {
            return;
        }

        Optional<Player> target;

        if(player.getUsername().equalsIgnoreCase(args[1])) {
            Message.FRIEND_REQUEST_SELF.send(player);
            return;
        }

        PlayerData playerData = plugin.getCache().getData(player.getUniqueId());

        switch (args[0].toLowerCase()) {
            case "add" -> {
                Set<String> friendUsernames = plugin.getCache()
                        .getData(player.getUniqueId())
                        .getFriends()
                        .stream()
                        .map(f -> f.getUsername().toLowerCase())
                        .collect(Collectors.toSet());

                String targetName = args[1].toLowerCase();

                if (friendUsernames.contains(targetName)) {
                    Message.ALREADY_FRIENDS.send(player);
                    return;
                }

                Optional<Player> targetOpt = plugin.getServer().getPlayer(args[1]);
                int targetId = -1;

                if (targetOpt.isPresent()) {
                    Player targetPlayer = targetOpt.get();
                    Message.FRIEND_REQUEST_SENT.send(player, "{player}", targetPlayer.getUsername());
                    targetId = plugin.getCache().getData(targetPlayer.getUniqueId()).getPlayerId();
                    Message.FRIEND_REQUEST.send(targetPlayer, "{player}", player.getUsername());
                } else {
                    targetId = plugin.getDatabase().getPlayerId(args[1]);
                    if (targetId == -1) {
                        player.sendMessage(CommonUtil.mm("<red>Player not found."));
                        return;
                    }
                    Message.FRIEND_REQUEST_SENT.send(player, "{player}", args[1]);
                }

                plugin.getDatabase().createFriendRequest(playerData.getPlayerId(), targetId);
            }
            case "remove" -> {
                plugin.getCache().getData(player.getUniqueId()).getFriends().forEach(f -> {
                    if(f.getUsername().equalsIgnoreCase(args[1])) {
                        plugin.getDatabase().removeFriendship(playerData.getPlayerId(), plugin.getDatabase().getPlayerId(f.getUsername()));
                        plugin.getCache().reloadFriends(player);
                        CommonUtil.mm("<red>You have removed them from your friend list.");
                    } else {
                        Message.NOT_FRIENDS.send(player);
                    }
                });
            }
            case "accept" -> {
                List<FriendRequest> requests = plugin.getDatabase().getFriendRequests(playerData.getPlayerId());
                for (FriendRequest r : requests) {
                    if(r.getSender().equalsIgnoreCase(args[1])) {
                        plugin.getDatabase().addFriendship(playerData.getPlayerId(), plugin.getDatabase().getPlayerId(r.getSender()));
                        Message.FRIEND_ACCEPT.send(player, "{player}", r.getSender());
                    } else {
                        Message.FRIEND_REQUEST_NOT_FOUND.send(player);
                    }
                }
            }
            case "deny" -> {
                List<FriendRequest> requests = plugin.getDatabase().getFriendRequests(playerData.getPlayerId());
                for (FriendRequest r : requests) {
                    if(r.getSender().equalsIgnoreCase(args[1])) {
                        plugin.getDatabase().removeFriendRequest(playerData.getPlayerId(), plugin.getDatabase().getPlayerId(r.getSender()));
                        Message.FRIEND_DENY.send(player, "{player}", r.getSender());
                    } else {
                        Message.FRIEND_REQUEST_NOT_FOUND.send(player);
                    }
                }
            }
            case "list" -> {
                Message.FRIEND_LIST_MESSAGE.send(player);
                plugin.getCache().getData(player.getUniqueId()).getFriends().forEach(f -> {
                    CommonUtil.mm("<aqua>" + f.getUsername() + ", <gray>|</gray> <white>Friends Since: <aqua>" + DateTimeFormatter.RFC_1123_DATE_TIME.format(f.getFriendSince()));
                });
            }
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return SimpleCommand.super.suggest(invocation);
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return SimpleCommand.super.suggestAsync(invocation);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return SimpleCommand.super.hasPermission(invocation);
    }
}
