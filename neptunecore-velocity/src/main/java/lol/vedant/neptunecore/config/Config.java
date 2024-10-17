package lol.vedant.neptunecore.config;

import lol.vedant.neptunecore.NeptuneCore;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.loader.ParsingException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Config {

    private ConfigurationLoader loader;
    private ConfigurationNode rootNode;
    private final String name;

    public Config(Path parentDir, String name) {
        this.name = name;
        try {
            if (!Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            Path filePath = parentDir.resolve(name);
            if (!Files.exists(filePath)) {
                Files.copy(Objects.requireNonNull(NeptuneCore.class.getClassLoader().getResourceAsStream(name)), filePath);
            }

            this.loader = YamlConfigurationLoader.builder().path(filePath).build();
            ConfigurationNode rootNode;
            this.rootNode = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyDefaults() {
        try {
            rootNode.mergeFrom(YamlConfigurationLoader.builder().url(NeptuneCore.class.getResource(name)).build().load(ConfigurationOptions.defaults()));
        } catch (ParsingException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            loader.save(rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigurationNode getConfig() {
        return rootNode;
    }

}