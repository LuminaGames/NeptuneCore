package lol.vedant.neptunecore.commands.other;

import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.utils.Message;
import lol.vedant.neptunecore.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class NeptuneCoreCommand extends Command {

    private NeptuneCore plugin;

    public NeptuneCoreCommand(String name, String permission, NeptuneCore plugin, String... aliases) {
        super(name, permission, aliases);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
            Message.HELP_1.send(sender);
            return;
        }

        sender.sendMessage(new TextComponent(Utils.cc("&7Running &b&lNeptune Core - &f" + plugin.getDescription().getVersion())));
        sender.sendMessage(new TextComponent(Utils.cc("&7Developed by &fLumina Games")));
    }
}
