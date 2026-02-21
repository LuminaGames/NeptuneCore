package lol.vedant.neptunecore.commands.staff;

import lol.vedant.neptunecore.managers.SocialSpyManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SocialSpyCommand extends Command {
    public SocialSpyCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        SocialSpyManager.toggle(player);

    }
}
