package lol.vedant.neptunecore.commands.friend;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.core.data.Friend;
import lol.vedant.core.data.FriendRequest;
import lol.vedant.core.utils.CommonUtil;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.database.Database;
import lol.vedant.neptunecore.utils.Message;
import net.kyori.adventure.text.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class FriendCommand implements SimpleCommand {

    private static final NeptuneCore plugin = NeptuneCore.getInstance();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy")
            .withZone(ZoneId.systemDefault());

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            Message.PLAYER_ONLY_COMMAND.send(invocation.source());
            return;
        }

        String[] args = invocation.arguments();

        if (args.length == 0) {
            sendHelp(player);
            return;
        }

        Database db = plugin.getDatabase();
        int playerId = db.getPlayerId(player.getUniqueId());

        switch (args[0].toLowerCase()) {
            case "add" -> {
                if (args.length < 2) {
                    Message.USAGE_FRIEND_ADD.send(player);
                    return;
                }

                String targetName = args[1];

                // Prevent self-friending
                if (player.getUsername().equalsIgnoreCase(targetName)) {
                    Message.FRIEND_REQUEST_SELF.send(player);
                    return;
                }

                // Resolve UUID from name
                int targetId = db.getPlayerId(targetName);
                if (targetId == -1) {
                    Message.PLAYER_NOT_FOUND.send(player, "{player}", targetName);
                    return;
                }

                List<Friend> friends = plugin.getCache().getPlayerData(player.getUniqueId()).getFriends();

                for (Friend f : friends) {
                    if(f.getUsername().equalsIgnoreCase(targetName)) {
                        Message.ALREADY_FRIENDS.send(player);
                        return;
                    }
                }

                // Create the request
                db.createFriendRequest(playerId, targetId);

                Message.FRIEND_REQUEST_SENT.send(player, "{player}", targetName);

                plugin.getServer().getPlayer(targetName).ifPresent(targetPlayer ->
                        Message.FRIEND_REQUEST.send(targetPlayer, "{player}", player.getUsername())
                );

                plugin.getCache().reloadFriends(player);
            }

            case "remove", "delete", "rem" -> {
                if (args.length < 2) {
                    Message.USAGE_FRIEND_REMOVE.send(player);
                    return;
                }

                String targetName = args[1];
                int targetId = db.getPlayerId(targetName);

                if (targetId == -1) {
                    Message.PLAYER_NOT_FOUND.send(player, "{player}", targetName);
                    return;
                }

                List<Friend> friends = plugin.getCache().getPlayerData(player.getUniqueId()).getFriends();;

                for (Friend f : friends) {
                    if (f.getUsername().equalsIgnoreCase(targetName)) {
                        db.removeFriend(playerId, targetId);
                        Message.FRIEND_REMOVE.send(player, "{player}", targetName);
                        plugin.getCache().reloadFriends(player);
                        return;
                    }
                }

                Message.NOT_FRIENDS.send(player);


            }

            case "accept" -> {
                if (args.length < 2) {
                    Message.USAGE_FRIEND_ACCEPT.send(player);
                    return;
                }

                String senderName = args[1];
                int senderId = db.getPlayerId(senderName);

                if (senderId == -1) {
                    Message.PLAYER_NOT_FOUND.send(player, "{player}", senderName);
                    return;
                }

                List<FriendRequest> requests = plugin.getCache().getPlayerData(player.getUniqueId()).getRequests();

                for (FriendRequest r : requests) {
                    if(r.getSender().equalsIgnoreCase(senderName)) {
                        db.addFriend(playerId, senderId);
                        Message.FRIEND_ACCEPT.send(player, "{player}", senderName);

                        plugin.getServer().getPlayer(senderName).ifPresent(p -> {
                            Message.FRIEND_ACCEPTED.send(p, "{player}", player.getUsername());
                            plugin.getCache().reloadFriends(p);
                        });

                        plugin.getCache().reloadFriends(player);
                        return;
                    }
                }

                Message.FRIEND_REQUEST_NOT_FOUND.send(player);
            }

            case "deny", "reject", "decline" -> {
                if (args.length < 2) {
                    Message.USAGE_FRIEND_DENY.send(player);
                    return;
                }

                String senderName = args[1];
                int senderId = db.getPlayerId(senderName);

                if (senderId == -1) {
                    Message.PLAYER_NOT_FOUND.send(player, "{player}", senderName);
                    return;
                }

                List<FriendRequest> requests = plugin.getCache().getPlayerData(player.getUniqueId()).getRequests();
                for (FriendRequest r : requests) {
                    if(r.getSender().equalsIgnoreCase(senderName)) {
                        db.removeFriendRequest(senderId, playerId);
                        Message.FRIEND_DENY.send(player, "{player}", senderName);
                        plugin.getCache().reloadFriends(player);
                    }
                }

                Message.FRIEND_REQUEST_NOT_FOUND.send(player);
            }

            case "list" -> {
                List<Friend> friends = plugin.getCache().getPlayerData(player.getUniqueId()).getFriends();

                if (friends.isEmpty()) {
                    Message.NO_FRIENDS.send(player);
                    return;
                }

                Message.FRIEND_LIST_MESSAGE.send(player);

                for (Friend friend : friends) {
                    Optional<Player> onlinePlayer = plugin.getServer().getPlayer(friend.getUsername());
                    String status = onlinePlayer.isPresent()
                            ? "<green>● Online"
                            : "<gray>● Offline";

                    Component line = CommonUtil.mm(String.format(
                            "<aqua>%s <dark_gray>|</dark_gray> %s <dark_gray>|</dark_gray> <gray>Since: <aqua>%s",
                            friend.getUsername(),
                            status,
                            DATE_FORMATTER.format(friend.getFriendSince())
                    ));
                    player.sendMessage(line);
                }
            }

            case "requests", "pending", "reqs" -> {
                List<FriendRequest> requests = plugin.getCache().getPlayerData(player.getUniqueId()).getRequests();

                if (requests.isEmpty()) {
                    Message.NO_PENDING_REQUESTS.send(player);
                    return;
                }

                player.sendMessage(CommonUtil.mm("<yellow>Incoming Requests:"));
                for (FriendRequest req : requests) {
                    player.sendMessage(CommonUtil.mm(String.format(
                            "  <aqua>%s <gray>- <click:run_command:/friend accept %s><hover:show_text:'<gray>Click to accept'>[ACCEPT]</hover></click>",
                            req.getSender(),
                            req.getSender()
                    )));
                }
            }
            /*

            case "block" -> {
                if (args.length < 2) {
                    player.sendMessage(CommonUtil.mm("<red>Usage: /friend block <player>"));
                    return;
                }

                Optional<UUID> targetUuidOpt = db.getPlayerUUID(args[1]);
                if (targetUuidOpt.isEmpty()) {
                    Message.PLAYER_NOT_FOUND.send(player, "{player}", args[1]);
                    return;
                }

                UUID targetUuid = targetUuidOpt.get();

                if (targetUuid.equals(playerUuid)) {
                    player.sendMessage(CommonUtil.mm("<red>You cannot block yourself."));
                    return;
                }

                boolean blocked = db.blockPlayer(playerUuid, targetUuid);
                if (blocked) {
                    player.sendMessage(CommonUtil.mm("<red>Blocked " + args[1]));
                    if (plugin.getCache() != null) {
                        plugin.getCache().reloadFriends(player);
                    }
                } else {
                    player.sendMessage(CommonUtil.mm("<gray>Player is already blocked."));
                }
            }

            case "unblock" -> {
                if (args.length < 2) {
                    player.sendMessage(CommonUtil.mm("<red>Usage: /friend unblock <player>"));
                    return;
                }

                Optional<UUID> targetUuidOpt = db.getPlayerUUID(args[1]);
                if (targetUuidOpt.isEmpty()) {
                    Message.PLAYER_NOT_FOUND.send(player, "{player}", args[1]);
                    return;
                }

                boolean unblocked = db.unblockPlayer(playerUuid, targetUuidOpt.get());
                if (unblocked) {
                    player.sendMessage(CommonUtil.mm("<green>Unblocked " + args[1]));
                } else {
                    player.sendMessage(CommonUtil.mm("<gray>Player was not blocked."));
                }
            }
             */

            default -> sendHelp(player);
        }
    }

    private void sendHelp(Player player) {
        Message.FRIEND_HELP.send(player);
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) return List.of();

        String[] args = invocation.arguments();
        Database db = plugin.getDatabase();

        if (args.length == 0 || args.length == 1) {
            String input = args.length == 0 ? "" : args[0].toLowerCase();
            return List.of("add", "remove", "accept", "deny", "list", "requests", "block", "unblock")
                    .stream()
                    .filter(cmd -> cmd.startsWith(input))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            String subCmd = args[0].toLowerCase();
            return switch (subCmd) {
                case "add" -> plugin.getServer().getAllPlayers().stream()
                        .map(Player::getUsername)
                        .filter(name -> !name.equalsIgnoreCase(player.getUsername()))
                        .collect(Collectors.toList());

                case "remove" -> plugin.getCache().getPlayerData(player.getUniqueId()).getFriends().stream()
                        .map(Friend::getUsername)
                        .collect(Collectors.toList());

                case "accept", "deny" -> plugin.getCache().getPlayerData(player.getUniqueId()).getRequests().stream()
                        .map(FriendRequest::getSender)
                        .collect(Collectors.toList());

                default -> List.of();
            };
        }

        return List.of();
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("neptune.friend");
    }
}