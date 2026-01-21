package lol.vedant.neptunecore.module.chat.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.module.chat.Chat;
import lol.vedant.neptunecore.module.chat.ChatManager;
import lol.vedant.neptunecore.module.staff.StaffModule;
import lol.vedant.neptunecore.utils.Message;

import java.util.UUID;

public class PlayerChatListener {

    private ChatManager chatManager;
    private NeptuneCore plugin = NeptuneCore.getInstance();

    public PlayerChatListener(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent e) {
        Player player = e.getPlayer();
        Chat channel = chatManager.getChatChannel(player.getUniqueId());

        switch (channel) {
            case STAFF_CHAT -> {
                e.setResult(PlayerChatEvent.ChatResult.denied());

                for (UUID uuid : StaffModule.onlineStaff) {
                    Player p = plugin.getServer().getPlayer(uuid).orElse(null);
                    Message.STAFF_CHAT_FORMAT.send(p, "{player}", player.getUsername(), "{message}", e.getMessage());
                }
            }
            case ADMIN_CHAT -> {
                e.setResult(PlayerChatEvent.ChatResult.denied());

                for (UUID uuid : StaffModule.onlineAdmins) {
                    Player p = plugin.getServer().getPlayer(uuid).orElse(null);
                    Message.STAFF_CHAT_FORMAT.send(p, "{player}", player.getUsername(), "{message}", e.getMessage());
                }
            }
            case PUBLIC_CHAT -> {
                //do nothing :)
            }
        }
    }

}
