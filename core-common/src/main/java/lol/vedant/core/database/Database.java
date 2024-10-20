package lol.vedant.core.database;

import lol.vedant.core.data.Friend;
import lol.vedant.core.data.UserSettings;

import java.util.List;
import java.util.UUID;

public interface Database {

    void init();

    UserSettings getUserSettings(UUID player);

    void saveUserSettings(UserSettings settings, UUID player);

    List<Friend> getFriends(UUID player);

    void saveFriends(List<Friend> friends);


}
