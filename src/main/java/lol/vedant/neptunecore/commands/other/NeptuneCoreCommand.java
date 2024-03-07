package lol.vedant.neptunecore.commands.other;

import lol.vedant.neptunecore.utils.Message;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class NeptuneCoreCommand extends Command {

    public NeptuneCoreCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(args[0].equalsIgnoreCase("help")) {
            Message.HELP_1.send(sender);
        }
    }
}
