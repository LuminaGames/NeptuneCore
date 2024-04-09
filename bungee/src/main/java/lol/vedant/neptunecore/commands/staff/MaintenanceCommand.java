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
    public void execute(CommandSender sender, String[] args) {

        if(args.length < 1) {
            if(plugin.getConfig().getBoolean("maintenance.enabled")) {
                plugin.getConfig().set("maintenance.enabled", false);
                plugin.saveConfig();
            } else {
                plugin.getConfig().set("maintenance.enabled", true);
                plugin.saveConfig();
            }
            return;
        }

        if(args[0].equalsIgnoreCase("on")) {
            plugin.getConfig().set("maintenance.enabled", true);
            plugin.saveConfig();
        } else if(args[0].equalsIgnoreCase("off")) {
            plugin.getConfig().set("maintenance.enabled", false);
            plugin.saveConfig();
        }

    }
}
