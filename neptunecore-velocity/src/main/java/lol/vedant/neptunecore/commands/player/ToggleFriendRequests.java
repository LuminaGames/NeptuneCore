package lol.vedant.neptunecore.commands.player;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.core.data.PlayerData;
import lol.vedant.core.data.Setting;
import lol.vedant.core.data.UserSettings;
import lol.vedant.neptunecore.NeptuneCore;

import java.util.List;

public class ToggleFriendRequests implements SimpleCommand {

    NeptuneCore plugin = NeptuneCore.getInstance();

    @Override
    public void execute(Invocation invocation) {
        if(invocation.source() instanceof Player player) {
            String[] args = invocation.arguments();

            PlayerData data = plugin.getCache().getPlayerData(player.getUniqueId());
            UserSettings settings = data.getSettings();

            if(args.length < 2) {
                String operation = args[0];
                if( operation.equalsIgnoreCase("on")) {
                    plugin.getDatabase().saveSetting(data.getPlayerId(), Setting.RECEIVE_FRIEND_REQUESTS, "true");
                    return;
                } else {
                    plugin.getDatabase().saveSetting(data.getPlayerId(), Setting.RECEIVE_FRIEND_REQUESTS, "false");
                    return;
                }
            }

            if(settings.getSetting(Setting.RECEIVE_FRIEND_REQUESTS).equalsIgnoreCase("true")) {
                plugin.getDatabase().saveSetting(data.getPlayerId(), Setting.RECEIVE_FRIEND_REQUESTS, "false");
            } else {
                plugin.getDatabase().saveSetting(data.getPlayerId(), Setting.RECEIVE_FRIEND_REQUESTS, "true");
            }

        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) return List.of();
        String[] args = invocation.arguments();

        if (args.length == 0 || args.length == 1) {
            String input = args.length == 0 ? "" : args[0].toLowerCase();
            return List.of("on", "off");
        }

        return List.of();
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return SimpleCommand.super.hasPermission(invocation);
    }
}
