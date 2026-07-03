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

public class MainConfig {
    private static YamlDocument config;
    public static YamlDocument get() {
        return config;
    }
    public static void setup() throws IOException {
        config = YamlDocument.create(
                new File(ArcadeCore.get().getDataFolder(), "config.yml"), Objects.requireNonNull(ArcadeCore.get().getResource("config.yml")),
                GeneralSettings.builder().setSerializer(SpigotSerializer.getInstance()).build(),
                UpdaterSettings.builder().setVersioning(new BasicVersioning("version")).build()
        );
    }
}
