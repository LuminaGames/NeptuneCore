package lol.vedant.neptunecore.commands.spy;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.module.spy.SpyModule;
import lol.vedant.neptunecore.utils.Message;

public class SocialSpyCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            Message.PLAYER_ONLY_COMMAND.send(invocation.source());
            return;
        }

        boolean toggle = SpyModule.toggleSocialSpy(player.getUniqueId());

        if(toggle) {
            Message.SOCIAL_SPY_ON.send(player);
        } else {
            Message.SOCIAL_SPY_OFF.send(player);
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("neptune.staff");
    }
}
