package lol.vedant.neptunecore.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import lol.vedant.neptunecore.utils.Message;

public class NeptuneCommand {

    public static BrigadierCommand createCommand(ProxyServer server) {
        LiteralCommandNode<CommandSource> neptuneCommand = BrigadierCommand.literalArgumentBuilder("neptune")
                .executes(ctx -> {
                    Message.HELP_1.send(ctx.getSource());
                    return Command.SINGLE_SUCCESS;
                }).build();
        return new BrigadierCommand(neptuneCommand);
    }

}
