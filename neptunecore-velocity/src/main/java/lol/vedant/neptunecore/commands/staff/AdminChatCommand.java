package lol.vedant.neptunecore.commands.staff;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.managers.AdminChatManager;
import lol.vedant.neptunecore.managers.StaffChatManager;
import lol.vedant.neptunecore.utils.Message;

public class AdminChatCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {

        if(!(invocation.source() instanceof Player)) {
            Message.PLAYER_ONLY_COMMAND.send(invocation.source());
            return;
        }

        Player player = (Player) invocation.source();
        String[] args = invocation.arguments();

        if(args.length > 0) {
            AdminChatManager.sendAdminChat(player, String.join(" ", args));
        } else {
            if(StaffChatManager.isEnabled(player)) {
                StaffChatManager.toggleStaffChat(player);
            }
            AdminChatManager.toggleAdminChat(player);
        }

    }
}
