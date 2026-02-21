package lol.vedant.neptunecore.module.maintenance;

import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.commands.staff.MaintenanceCommand;
import lol.vedant.neptunecore.module.maintenance.events.PlayerJoinListener;
import lol.vedant.core.module.Module;

public class MaintenanceModule implements Module {
    
    private NeptuneCore plugin = NeptuneCore.getInstance();
    
    @Override
    public void enable() {
        plugin.getServer().getEventManager().register(plugin, new PlayerJoinListener(plugin));
        NeptuneCore.registerCommand("maintenance", MaintenanceCommand.createCommand(NeptuneCore.server));
    }

    @Override
    public void disable() {

    }

    @Override
    public String getName() {
        return "Maintenance";
    }
}
