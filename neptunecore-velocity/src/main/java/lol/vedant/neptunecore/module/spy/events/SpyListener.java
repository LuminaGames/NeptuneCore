package lol.vedant.neptunecore.module.spy.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.module.spy.SpyModule;
import lol.vedant.neptunecore.module.staff.StaffModule;

public class SpyListener {

    NeptuneCore plugin = NeptuneCore.getInstance();

    @Subscribe
    public void onPlayerChat(com.velocitypowered.api.event.player.PlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();

        //if (StaffModule.onlineStaff.contains(player.getUniqueId())) return;

        SpyModule.socialSpy.forEach(uuid ->
                plugin.getServer().getPlayer(uuid).ifPresent(staff ->
                        SpyModule.sendMessageSpy(player, message)
                )
        );

    }

    @Subscribe
    public void onCommandExecute(CommandExecuteEvent e) {
        if (!(e.getCommandSource() instanceof Player player)) return;
        String command = e.getCommand();

        //if (StaffModule.onlineStaff.contains(player.getUniqueId())) return;

        SpyModule.commandSpy.forEach(uuid ->
                plugin.getServer().getPlayer(uuid).ifPresent(staff ->
                        SpyModule.sendCommandSpy(player, command)
                )
        );
    }

}
