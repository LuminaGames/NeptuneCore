package lol.vedant.neptunecore.commands.player;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.managers.MessageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Optional;

import static lol.vedant.neptunecore.NeptuneCore.server;

public class MessageCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if(!(invocation.source() instanceof Player)) {
            invocation.source().sendPlainMessage("This command can only be used by a player.");
            return;
        }

        Player player = (Player) invocation.source();
        String[] args = invocation.arguments();

        if (args.length < 2) {
            player.sendMessage(Component.text("Usage: /message <player> <message>", NamedTextColor.RED));
            return;
        }

        String playerName = args[0];
        Optional<Player> targetOptional = server.getPlayer(playerName);

        if (targetOptional.isEmpty() || !targetOptional.get().isActive()) {
            player.sendMessage(Component.text("Player " + playerName + " is not online.", NamedTextColor.RED));
            return;
        }

        Player target = targetOptional.get();

        if(target == player) {
            player.sendMessage(Component.text("You cannot message yourself", NamedTextColor.RED));
            return;
        }

        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }

        MessageManager.message(player, target, message.toString().trim());
    }
}
