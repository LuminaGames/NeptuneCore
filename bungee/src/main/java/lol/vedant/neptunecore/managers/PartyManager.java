package lol.vedant.neptunecore.managers;

import lol.vedant.neptunecore.api.party.Party;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PartyManager implements Party {

    private static List<PartyManager.Party> parites = new ArrayList<>();

    @Override
    public boolean hasParty(ProxiedPlayer player) {
        for (Party party : getParites()) {
            if (party.members.contains(player)) return true;
        }
        return false;
    }

    @Override
    public boolean isLeader(ProxiedPlayer player) {
        for (Party party : getParites()) {
            if (party.members.contains(player)) {
                if (party.leader == player) return true;
            }
        }
        return false;
    }

    @Override
    public List<ProxiedPlayer> getMembers(ProxiedPlayer player) {
        for (Party party : getParites()) {
            if (party.members.contains(player)) {
                return party.members;
            }
        }
        return null;
    }

    @Override
    public void create(ProxiedPlayer leader, ProxiedPlayer... players) {
        Party p = new Party(leader);
        p.addMember(leader);
        for (ProxiedPlayer mem : players) {
            p.addMember(mem);
        }
    }

    @Override
    public void add(ProxiedPlayer leader, ProxiedPlayer player) {
        if (leader == null || player == null) return;
        PartyManager.Party p = getParty(leader);
        if (p == null) return;
        p.addMember(player);
    }

    @Nullable
    private Party getParty(ProxiedPlayer owner) {
        for (Party p : getParites()) {
            if (p.getOwner() == owner) return p;
        }
        return null;
    }

    @Override
    public void remove(ProxiedPlayer leader, ProxiedPlayer player) {
        Party p = getParty(leader);
        if (p != null) {
            if (p.members.contains(player)) {
                for (ProxiedPlayer mem : p.members) {
                    // Send remove message
                }
                p.members.remove(leader);
                if (p.members.isEmpty() || p.members.size() == 1) {
                    disband(p.leader);
                    parites.remove(p);
                }
            }
        }
    }

    @Override
    public void disband(ProxiedPlayer leader) {
        PartyManager.Party pa = getParty(leader);
        if (pa == null) return;
        for (ProxiedPlayer p : pa.members) {
            // send disband success message
        }
        pa.members.clear();
        PartyManager.parites.remove(pa);
    }

    @Override
    public boolean isMember(ProxiedPlayer leader, ProxiedPlayer player) {
        for (Party p : parites) {
            if (p.leader == leader) {
                if (p.members.contains(player)) return true;
            }
        }
        return false;
    }

    @Override
    public ProxiedPlayer getLeader(ProxiedPlayer member) {
        for (PartyManager.Party party: PartyManager.getParites()) {
            if (party.members.contains(member)){
                return party.leader;
            }
        }
        return null;
    }

    public static List<Party> getParites() {
        return Collections.unmodifiableList(parites);
    }

    static class Party {

        private List<ProxiedPlayer> members = new ArrayList<>();
        private ProxiedPlayer leader;

        public Party(ProxiedPlayer p) {
            leader = p;
            PartyManager.parites.add(this);
        }

        public ProxiedPlayer getOwner() {
            return leader;
        }

        void addMember(ProxiedPlayer p) {
            members.add(p);
        }
    }
}
