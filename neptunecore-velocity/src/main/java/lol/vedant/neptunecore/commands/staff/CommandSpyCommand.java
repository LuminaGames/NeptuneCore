package lol.vedant.neptunecore.commands.staff;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.managers.CommandSpyManager;

public class CommandSpyCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if(!(invocation.source() instanceof Player)) {
            return;
        }

        Player player = (Player) invocation.source();

        CommandSpyManager.toggle(player);

    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("neptune.command.commandspy");
    }
}
