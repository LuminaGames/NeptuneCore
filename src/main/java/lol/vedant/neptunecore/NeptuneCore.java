package lol.vedant.neptunecore;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;


public final class NeptuneCore extends Plugin {

    private Configuration config;
    private Configuration messages;

    @Override
    public void onEnable() {
        initConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void initConfig() {
        try {
            makeConfig("config.yml", false);
            makeConfig("messages.yml", false);
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
            messages = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "messages.yml"));
         } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeConfig(String fName, Boolean empty) throws IOException {
        File file = new File(getDataFolder(), fName);
        if (!file.exists()) {
            if (empty) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try (InputStream in = getResourceAsStream(fName)) {
                    Files.copy(in, file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Configuration getConfig() {
        return config;
    }

    public Configuration getMessages() {
        return config;
    }
}
