package lol.vedant.neptunecore.commands.staff;

import lol.vedant.neptunecore.managers.AdminChatManager;
import lol.vedant.neptunecore.managers.StaffChatManager;
import lol.vedant.neptunecore.utils.Message;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class AdminChatCommand extends Command {
    public AdminChatCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(Message.PLAYER_ONLY_COMMAND.asString()));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if(args.length > 0) {
            AdminChatManager.sendAdminChat(player, String.join(" ", args));
        } else {
            if(StaffChatManager.isEnabled(player)) {
                StaffChatManager.toggleStaffChat(player);
            }
            AdminChatManager.toggleAdminChat(player);
        }

    }
}
