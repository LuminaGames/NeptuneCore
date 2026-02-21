package lol.vedant.neptunecore.module.staff;

import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.core.module.Module;
import lol.vedant.neptunecore.module.staff.events.StaffJoinListener;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StaffModule implements Module {

    public static Set<UUID> onlineStaff = ConcurrentHashMap.newKeySet();
    public static Set<UUID> onlineAdmins = ConcurrentHashMap.newKeySet();

    private NeptuneCore plugin = NeptuneCore.getInstance();

    @Override
    public void enable() {
        plugin.getServer().getEventManager().register(plugin, new StaffJoinListener());
    }

    @Override
    public void disable() {

    }

    @Override
    public String getName() {
        return "Staff";
    }
}
