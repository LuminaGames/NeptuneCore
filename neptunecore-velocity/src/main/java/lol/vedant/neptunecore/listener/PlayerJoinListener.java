package lol.vedant.neptunecore.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.core.utils.CommonUtil;
import lol.vedant.neptunecore.NeptuneCore;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;

public class PlayerJoinListener {

    private final NeptuneCore plugin;

    public PlayerJoinListener(NeptuneCore plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPlayerJoin(ServerConnectedEvent e) {
        Player player = e.getPlayer();

        if(plugin.getConfig().node("maintenance", "enabled").getBoolean()) {
            try {
            player.disconnect(CommonUtil.mm(
                CommonUtil.fromList(NeptuneCore.getInstance().getConfig().node("maintenance", "kick-message").getList(String.class))
            ));
            } catch (SerializationException ex) {
                ex.printStackTrace();
            }
            return;
        }

        plugin.getCache().loadPlayerData(player);
    }
}
