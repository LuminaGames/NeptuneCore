package lol.vedant.core.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserSettings {

    Map<Setting, String> userSettings = new HashMap<>();

    public String getSetting(Setting type) {
        return userSettings.get(type);
    }

    public Map<Setting, String> getAllSettings() {
        return userSettings;
    }

    public void setUserSettings(HashMap<Setting, String> settings) {
        userSettings = settings;
    }

}
