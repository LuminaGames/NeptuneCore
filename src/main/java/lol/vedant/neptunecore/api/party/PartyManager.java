package lol.vedant.neptunecore.api.party;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public interface PartyManager {

    Party getParty();

    Party createParty(ProxiedPlayer player);

    void disbandParty(Party party);

}
