package lol.vedant.neptunecore.commands.player;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.managers.MessageManager;
import lol.vedant.neptunecore.utils.Message;

import java.util.Optional;

import static lol.vedant.neptunecore.NeptuneCore.server;

public class ReplyCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if(!(invocation.source() instanceof Player)) {
            return;
        }

        Player player = (Player) invocation.source();
        String[] args = invocation.arguments();
        String message = String.join(" ", args);

        if(MessageManager.hasLastMessage(player)) {
            Optional<Player> targetOptional = server.getPlayer(MessageManager.getLastMessage(player).getUsername());
            if(targetOptional.isPresent() && targetOptional.get().isActive()) {
                MessageManager.message(player, targetOptional.get(), message);
            } else {
                Message.OFFLINE_PLAYER.send(player);
            }
        } else {
            Message.NO_ONE_REPLY.send(player);
        }
    }
}
