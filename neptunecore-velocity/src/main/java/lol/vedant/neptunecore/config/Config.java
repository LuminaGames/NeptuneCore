package lol.vedant.neptunecore.config;

import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Config {

    private final YamlFile yamlFile;

    public Config(Path path, String fileName) {

        if(!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Path configPath = path.resolve(fileName);

        if(!Files.exists(configPath)) {
            try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
                Files.copy(Objects.requireNonNull(in), configPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.yamlFile = new YamlFile(configPath.toFile());

        try  {
            this.yamlFile.load();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public YamlFile getConfig() {
        return this.yamlFile;
    }

    public void reload() {
        try {
            yamlFile.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}