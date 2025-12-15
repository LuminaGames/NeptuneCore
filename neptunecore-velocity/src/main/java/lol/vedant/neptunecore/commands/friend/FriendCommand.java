package lol.vedant.neptunecore.commands.friend;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import lol.vedant.core.data.Friend;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.utils.Message;

import java.util.List;

public class FriendCommand {

    private static NeptuneCore plugin = NeptuneCore.getInstance();

    public static BrigadierCommand createCommand(final ProxyServer proxy) {
        LiteralCommandNode<CommandSource> friendCommand =
                BrigadierCommand.literalArgumentBuilder("friend")
                        .requires(source -> source.hasPermission("neptune.command.friend"))

                        // /friend
                        .executes(ctx -> {
                            if (!(ctx.getSource() instanceof Player)) {
                                ctx.getSource().sendPlainMessage("This command can only be executed by player.");
                                return Command.SINGLE_SUCCESS;
                            }
                            ctx.getSource().sendPlainMessage("Friend help message");
                            return Command.SINGLE_SUCCESS;
                        })

                        // /friend <task>
                        .then(
                                BrigadierCommand.requiredArgumentBuilder("task", StringArgumentType.word())
                                        .executes(ctx -> {
                                            Player sender = (Player) ctx.getSource();
                                            String task = ctx.getArgument("task", String.class);

                                            //sender.sendMessage("Usage: /friend " + task + " <username>");
                                            return Command.SINGLE_SUCCESS;
                                        })

                                        // /friend <task> <username>
                                        .then(
                                                BrigadierCommand.requiredArgumentBuilder("username", StringArgumentType.word())
                                                        .executes(ctx -> {
                                                            Player sender = (Player) ctx.getSource();
                                                            String task = ctx.getArgument("task", String.class);
                                                            String username = ctx.getArgument("username", String.class);

                                                            switch (task.toLowerCase()) {
                                                                case "add" -> {
                                                                    List<Friend> senderFriends = plugin.getDatabase().getFriends(sender.getUniqueId());

                                                                    for (Friend f : senderFriends) {
                                                                        if (f.getUsername().equalsIgnoreCase(username)) {
                                                                            Message.ALREADY_FRIENDS.send(sender);
                                                                            return Command.SINGLE_SUCCESS;
                                                                        }
                                                                    }

                                                                    Message.FRIEND_REQUEST_SENT.send(sender);

                                                                    //Send friend req message if player is online
                                                                    if(proxy.getPlayer(username).isPresent()) {
                                                                        Message.FRIEND_REQUEST.send(proxy.getPlayer(username).get());
                                                                    }
                                                                }
                                                                case "remove" -> {
                                                                    List<Friend> senderFriends = plugin.getDatabase().getFriends(sender.getUniqueId());

                                                                    for (Friend f : senderFriends) {
                                                                        if (f.getUsername().equalsIgnoreCase(username)) {
                                                                            Message.NOT_FRIENDS.send(sender);
                                                                            return Command.SINGLE_SUCCESS;
                                                                        }
                                                                    }


                                                                }
                                                                case "accept" -> {
                                                                    List<Friend> senderFriends = plugin.getDatabase().getFriends(sender.getUniqueId());

                                                                    for (Friend f : senderFriends) {
                                                                        if (f.getUsername().equalsIgnoreCase(username)) {
                                                                            Message.ALREADY_FRIENDS.send(sender);
                                                                            return Command.SINGLE_SUCCESS;
                                                                        }
                                                                    }
                                                                }
                                                                case "deny" -> {
                                                                    //Deny a friend request
                                                                }
                                                                default -> {
                                                                    //Unknown command maybe send help message here.
                                                                }
                                                            }

                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                        )
                        )
                        .build();
        return new BrigadierCommand(friendCommand);
    }

}
