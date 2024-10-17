package lol.vedant.neptunecore.api.party;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public interface Party {

    boolean hasParty(ProxiedPlayer player);

    boolean isLeader(ProxiedPlayer player);

    List<ProxiedPlayer> getMembers(ProxiedPlayer player);

    void create(ProxiedPlayer leader, ProxiedPlayer... players);

    void add(ProxiedPlayer leader, ProxiedPlayer player);

    void remove(ProxiedPlayer leader, ProxiedPlayer player);

    void disband(ProxiedPlayer leader);

    boolean isMember(ProxiedPlayer leader, ProxiedPlayer player);

    default ProxiedPlayer getLeader(ProxiedPlayer member) {
        for(ProxiedPlayer p : this.getMembers(member)) {
            return p;
        }
        return null;
    }





}
