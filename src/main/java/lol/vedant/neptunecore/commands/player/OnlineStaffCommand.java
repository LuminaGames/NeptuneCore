package lol.vedant.neptunecore.commands.player;

import lol.vedant.neptunecore.utils.Message;
import lol.vedant.neptunecore.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OnlineStaffCommand extends Command {


    public OnlineStaffCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Collection<ProxiedPlayer> players = ProxyServer.getInstance().getPlayers();
        List<String> onlineStaff = new ArrayList<>();
        for (ProxiedPlayer player: players) {
            if(player.hasPermission("neptune.staff")) {
                onlineStaff.add(player.getName());
            }
        }
        Message.STAFF_ONLINE.send(sender, "{staffList}", Utils.fromList(onlineStaff));

    }
}
