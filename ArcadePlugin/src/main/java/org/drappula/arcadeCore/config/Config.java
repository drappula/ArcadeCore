package org.drappula.arcadeCore.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import dev.dejvokep.boostedyaml.spigot.SpigotSerializer;
import org.drappula.arcadeCore.ArcadeCore;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Config {
    private static String fileName;
    public Config(String configFileName) {
        fileName = configFileName;
    }
    public static YamlDocument config;
    public static YamlDocument get() {
        return config;
    }
    public static void setup() {
        try {
            config = YamlDocument.create(
                    new File(ArcadeCore.get().getDataFolder(), fileName), Objects.requireNonNull(ArcadeCore.get().getResource(fileName)),
                    GeneralSettings.builder().setSerializer(SpigotSerializer.getInstance()).build(),
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("version")).build()
            );
        } catch (IOException e) {
            ArcadeCore.get().getSLF4JLogger().error("An error occurred while setting up {} configuration.", fileName, e);
        }
    }
}
