package lol.vedant.neptunecore.commands.staff;

import lol.vedant.neptunecore.NeptuneCore;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class MaintenanceCommand extends Command {

    private NeptuneCore plugin;

    public MaintenanceCommand(String name, String permission, NeptuneCore plugin, String... aliases) {
        super(name, permission, aliases);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {

    }
}
