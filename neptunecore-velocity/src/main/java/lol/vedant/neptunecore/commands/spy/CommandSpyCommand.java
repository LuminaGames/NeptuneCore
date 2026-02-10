package lol.vedant.neptunecore.commands.spy;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.module.spy.SpyModule;
import lol.vedant.neptunecore.utils.Message;

public class CommandSpyCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            Message.PLAYER_ONLY_COMMAND.send(invocation.source());
            return;
        }
        boolean toggle = SpyModule.toggleCommandSpy(player.getUniqueId());

        if(toggle) {
            Message.COMMAND_SPY_ON.send(player);
        } else {
            Message.COMMAND_SPY_OFF.send(player);
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return SimpleCommand.super.hasPermission(invocation);
    }
}
