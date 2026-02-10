package lol.vedant.neptunecore.module.message;

import com.velocitypowered.api.proxy.Player;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.event.PlayerMessageEvent;
import lol.vedant.neptunecore.module.Module;
import lol.vedant.neptunecore.utils.Message;

import java.util.HashMap;
import java.util.UUID;

public class MessageModule implements Module {

    public static HashMap<UUID, UUID> lastMessage = new HashMap<>();
    static NeptuneCore plugin = NeptuneCore.getInstance();

    @Override
    public void enable() {
        //Empty
    }

    @Override
    public void disable() {

    }

    @Override
    public String getName() {
        return "Message";
    }

    public static void sendMessage(Player player, Player target, String message) {
        Message.MESSAGE_FORMAT.send(player,
                "{player}", target.getUsername(),
                "{message}", message);

        Message.MESSAGE_FORMAT.send(target,
                "{player}", player.getUsername(),
                "{message}", message);

        //Fire message event
        plugin.getServer().getEventManager().fire(new PlayerMessageEvent(player, target, message));
        // Store last message
        lastMessage.put(player.getUniqueId(), target.getUniqueId());
        lastMessage.put(target.getUniqueId(), player.getUniqueId());
    }

    public static UUID getLastMessage(UUID player) {
        return lastMessage.get(player);
    }
}
