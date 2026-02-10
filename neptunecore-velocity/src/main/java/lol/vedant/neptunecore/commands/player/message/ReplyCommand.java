package lol.vedant.neptunecore.commands.player.message;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.core.utils.CommonUtil;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.module.message.MessageModule;
import lol.vedant.neptunecore.utils.Message;

import java.util.Arrays;
import java.util.UUID;

public class ReplyCommand implements SimpleCommand {

    private final NeptuneCore plugin = NeptuneCore.getInstance();

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            Message.PLAYER_ONLY_COMMAND.send(invocation.source());
            return;
        }

        String[] args = invocation.arguments();

        if (args.length < 1) {
            player.sendMessage(CommonUtil.mm("Usage: /reply <message>"));
            return;
        }

        UUID last = MessageModule.lastMessage.get(player.getUniqueId());

        if (last == null) {
            Message.NO_ONE_REPLY.send(player);
            return;
        }

        Player target = plugin.getServer().getPlayer(last).orElse(null);

        if (target == null) {
            Message.OFFLINE_PLAYER.send(player);
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

        MessageModule.sendMessage(player, target, message);
    }
}
