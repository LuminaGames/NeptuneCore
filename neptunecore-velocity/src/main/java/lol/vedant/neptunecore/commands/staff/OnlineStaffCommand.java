package lol.vedant.neptunecore.commands.staff;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import lol.vedant.neptunecore.NeptuneCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.stream.Collectors;

public class OnlineStaffCommand implements SimpleCommand {

    private final ProxyServer proxyServer = NeptuneCore.server;

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            invocation.source().sendMessage(MiniMessage.miniMessage().deserialize("<red>Only players can use this command.</red>"));
            return;
        }

        String staffList = proxyServer.getAllPlayers().stream()
                .filter(p -> p.hasPermission("neptune.staff"))
                .map(Player::getUsername)
                .collect(Collectors.joining(", "));


        if (staffList.isEmpty()) {
            player.sendMessage(Component.text("<red>No staff members are currently online.</red>"));
        } else {
            player.sendMessage(Component.text("<green>Online Staff:</green> <white>" + staffList + "</white>"));
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("neptune.staff");
    }
}
