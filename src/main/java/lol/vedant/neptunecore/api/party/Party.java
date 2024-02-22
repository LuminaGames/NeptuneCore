package lol.vedant.neptunecore.api.party;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public interface Party {

    ProxiedPlayer getLeader();

    List<ProxiedPlayer> getMembers();

    boolean addMember(ProxiedPlayer player, ProxiedPlayer leader);

    boolean removeMember(ProxiedPlayer leader);

    boolean isMember(ProxiedPlayer leader);

    void sendMessage(ProxiedPlayer leader, String message);



}
