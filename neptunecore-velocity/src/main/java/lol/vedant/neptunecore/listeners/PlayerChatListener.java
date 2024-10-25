package lol.vedant.neptunecore.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.managers.AdminChatManager;
import lol.vedant.neptunecore.managers.CommandSpyManager;
import lol.vedant.neptunecore.managers.StaffChatManager;
import lol.vedant.neptunecore.utils.Message;

import java.util.List;


public class PlayerChatListener {

    @Subscribe
    public void onCommandExecution(PlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();
        if(!message.startsWith("/")) {
            if(StaffChatManager.isEnabled(player)) {
                e.setResult(PlayerChatEvent.ChatResult.denied());
                StaffChatManager.sendStaffChat(player, message);
            }
            if (AdminChatManager.isEnabled(player)) {
                e.setResult(PlayerChatEvent.ChatResult.denied());
                AdminChatManager.sendAdminChat(player, message);
            }
        }

        if(message.startsWith("/")) {
            List<Player> staffPlayers = CommandSpyManager.getToggledPlayers();

            if(staffPlayers.isEmpty()) {
                return;
            }

            for (Player staff : staffPlayers) {
                Message.COMMAND_SPY.send(staff,
                        "{sender}", player.getUsername(),
                        "{command}", message);
            }
        }
    }

}
