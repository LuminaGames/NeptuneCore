package lol.vedant.core.data;

import java.util.UUID;

public class UserSettings {

    String username;
    UUID uuid;
    boolean personalMessageEnabled;
    boolean friendRequestEnabled;
    boolean partyInviteEnabled;
    boolean hiddenStaff;

    public UserSettings(String username, UUID uuid, boolean personalMessageEnabled, boolean friendRequestEnabled, boolean partyInviteEnabled, boolean hiddenStaff) {
        this.username = username;
        this.uuid = uuid;
        this.personalMessageEnabled = personalMessageEnabled;
        this.friendRequestEnabled = friendRequestEnabled;
        this.partyInviteEnabled = partyInviteEnabled;
        this.hiddenStaff = hiddenStaff;
    }

    public String getUsername() {
        return username;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isPersonalMessageEnabled() {
        return personalMessageEnabled;
    }

    public boolean isFriendRequestEnabled() {
        return friendRequestEnabled;
    }

    public boolean isPartyInviteEnabled() {
        return partyInviteEnabled;
    }

    public boolean isHiddenStaff() {
        return hiddenStaff;
    }
}
