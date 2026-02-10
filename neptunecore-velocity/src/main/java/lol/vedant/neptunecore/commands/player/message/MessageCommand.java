package lol.vedant.neptunecore.commands.player.message;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.module.message.MessageModule;
import lol.vedant.neptunecore.utils.Message;

public class MessageCommand implements SimpleCommand {

    private final NeptuneCore plugin = NeptuneCore.getInstance();

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            Message.PLAYER_ONLY_COMMAND.send(invocation.source());
            return;
        }

        String[] args = invocation.arguments();

        if (args.length < 2) {
            Message.NO_PLAYER_SPECIFIED.send(player);
            return;
        }

        Player target = plugin.getServer().getPlayer(args[0]).orElse(null);

        if (target == null) {
            Message.PLAYER_NOT_FOUND.send(player);
            return;
        }

        //if (target.equals(player)) {
        //    player.sendMessage(Message.text("You cannot message yourself."));
        //    return;
        //}

        String message = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));

        // TODO: check if personal messages are enabled

        MessageModule.sendMessage(player, target, message);
    }
}
