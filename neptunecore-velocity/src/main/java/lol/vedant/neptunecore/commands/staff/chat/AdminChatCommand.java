package lol.vedant.neptunecore.commands.staff.chat;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.module.chat.Chat;
import lol.vedant.neptunecore.module.chat.ChatManager;
import lol.vedant.neptunecore.module.staff.StaffModule;
import lol.vedant.neptunecore.utils.Message;

import java.util.UUID;

public class AdminChatCommand implements SimpleCommand {

    NeptuneCore plugin = NeptuneCore.getInstance();

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            Message.PLAYER_ONLY_COMMAND.send(invocation.source());
            return;
        }

        if(!player.hasPermission("neptune.admin.chat")) {
            Message.NO_PERMISSION.send(player);
            return;
        }

        String[] args = invocation.arguments();

        if (args.length > 0) {
            String message = String.join(" ", args);
            for (UUID uuid : StaffModule.onlineAdmins) {
                Player target = plugin.getServer().getPlayer(uuid).orElse(null);
                Message.ADMIN_CHAT_FORMAT.send(target, "{player}", player.getUsername(), "{message}", message);
            }
            return;
        }

        ChatManager.setChatChannel(player.getUniqueId(), Chat.ADMIN_CHAT);
        Message.ADMIN_CHAT_ON.send(player);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("neptune.admin.chat");
    }
}
