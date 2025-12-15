package lol.vedant.neptunecore.commands.friend;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;

public class FriendCommand {

    public static BrigadierCommand createCommand(final ProxyServer proxy) {
        LiteralCommandNode<CommandSource> friendCommand = BrigadierCommand.literalArgumentBuilder("friend")
                .requires(source -> source.hasPermission("neptune.command.friend"))
                .executes(ctx -> {
                    // display friend help message
                    return Command.SINGLE_SUCCESS;
                }).then(BrigadierCommand.requiredArgumentBuilder("task", StringArgumentType.word()))
                .executes(ctx -> {
                    String task = ctx.getArgument("task", String.class);
                    switch (task) {
                        case "add" -> {
                            //Send Friend Request
                        }
                        case "remove" -> {
                            //Remove friend from list
                        }
                        case "accept" -> {
                            //Accept a incoming request
                        }
                        case "deny" -> {
                            //Deny Incoming request
                        }
                    }
                    return Command.SINGLE_SUCCESS;
                }).build();
        return new BrigadierCommand(friendCommand);
    }

}
