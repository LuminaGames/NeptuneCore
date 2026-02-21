package lol.vedant.neptunecore.module.spy;

import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.core.module.Module;
import lol.vedant.neptunecore.module.spy.events.SpyListener;
import lol.vedant.neptunecore.module.staff.StaffModule;
import lol.vedant.neptunecore.utils.Message;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SpyModule implements Module {

    static NeptuneCore plugin = NeptuneCore.getInstance();
    public static Set<UUID> socialSpy = new HashSet<>();
    public static Set<UUID> commandSpy = new HashSet<>();

    @Override
    public void enable() {
        plugin.getServer().getEventManager().register(plugin, new SpyListener());
    }

    @Override
    public void disable() {

    }

    @Override
    public String getName() {
        return "Spy";
    }

    public static void sendCommandSpy(Player player, String command) {
        for (UUID uuid : StaffModule.onlineStaff) {
            Player staff = plugin.getServer().getPlayer(uuid).orElse(null);
            Message.COMMAND_SPY.send(staff, "{sender}", player.getUsername(), "{command}", command);
        }
    }

    public static void sendMessageSpy(Player player, Player receiver, String message) {
        for (UUID uuid : StaffModule.onlineStaff) {
            Player staff = plugin.getServer().getPlayer(uuid).orElse(null);
            Message.SOCIAL_SPY.send(staff, "{sender}", player.getUsername(), "{message}", message, "{receiver}", receiver.getUsername());
        }
    }

    public static boolean toggleSocialSpy(UUID uuid) {
        if (socialSpy.contains(uuid)) {
            socialSpy.remove(uuid);
            return false;
        } else {
            socialSpy.add(uuid);
            return true;
        }
    }

    public static boolean toggleCommandSpy(UUID uuid) {
        if (commandSpy.contains(uuid)) {
            commandSpy.remove(uuid);
            return false;
        } else {
            commandSpy.add(uuid);
            return true;
        }
    }
}
