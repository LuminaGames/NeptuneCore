package lol.vedant.neptunecore.module.staff.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.utils.Message;

import java.util.UUID;

import static lol.vedant.neptunecore.module.staff.StaffModule.onlineAdmins;
import static lol.vedant.neptunecore.module.staff.StaffModule.onlineStaff;

public class StaffJoinListener {


    NeptuneCore plugin = NeptuneCore.getInstance();

    @Subscribe
    public void onJoin(ServerConnectedEvent e) {
        Player player = e.getPlayer();

        if(player.hasPermission("neptune.staff")) {
            onlineStaff.add(player.getUniqueId());
        }

        if(player.hasPermission("neptune.admin")) {
            onlineAdmins.add(player.getUniqueId());
        }

        for (UUID uuid : onlineStaff) {
            Player staff = plugin.getServer().getPlayer(uuid).orElse(null);

            if(e.getPreviousServer().isPresent()) {
                Message.SERVER_SWITCH.send(staff,
                        "{player}", player.getUsername(),
                        "{targetServer}", e.getServer().getServerInfo().getName(),
                        "{currentServer}", e.getPreviousServer().get().getServerInfo().getName()
                );
            } else {
                Message.SERVER_JOIN.send(staff, "{player}", player.getUsername(), "{serverName}", e.getServer().getServerInfo().getName());
            }

        }
    }

    @Subscribe
    public void onLeave(DisconnectEvent e) {
        onlineStaff.remove(e.getPlayer().getUniqueId());
    }

}
