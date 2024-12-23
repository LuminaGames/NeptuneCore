package lol.vedant.neptunecore.commands.friends;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lol.vedant.core.data.Friend;
import lol.vedant.neptunecore.managers.PlayerDataManager;
import lol.vedant.neptunecore.utils.Message;
import net.kyori.adventure.text.Component;

import java.time.Instant;
import java.util.List;

public class FriendCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if(!(invocation.source() instanceof Player)) {
            Message.PLAYER_ONLY_COMMAND.send(invocation.source());
            return;
        }

        Player player = (Player) invocation.source();
        String[] args = invocation.arguments();
        List<Friend> playerFriends = PlayerDataManager.getPlayerData(player).getFriends();
        if(args.length == 0) {
            Message.FRIEND_HELP.send(player);
        } else if(args[0].equalsIgnoreCase("list")) {
            Message.FRIEND_LIST_MESSAGE.send(player);
            playerFriends.forEach(f -> {
                player.sendMessage(Component.text(f.getUsername()));
            });
        } else if(args[0].equalsIgnoreCase("add")) {

            if(args.length == 1) {
                Message.NO_PLAYER_SPECIFIED.send(player);
                return;
            }
            PlayerDataManager.getPlayerData(player).getFriends().add(new Friend(args[1], Instant.now()));
            //Make a friend request system
            //Add a saveData method in PlayerDataManager
            //Add a refresh dataMethod in PlayerDataManager
        } else if(args[0].equalsIgnoreCase("remove")) {
            if(args.length == 1) {
                Message.NO_PLAYER_SPECIFIED.send(player);
                return;
            }
            PlayerDataManager.getPlayerData(player).getFriends().forEach(f -> {
                if(f.getUsername().equals(args[1])) {
                    PlayerDataManager.getPlayerData(player).getFriends().remove(f);
                }
            });

            //Save and refresh the data in cache
        } else if (args[0].equalsIgnoreCase("accept")) {

        } else if(args[0].equalsIgnoreCase("deny")) {

        }

    }



    @Override
    public boolean hasPermission(Invocation invocation) {
        return SimpleCommand.super.hasPermission(invocation);
    }
}
