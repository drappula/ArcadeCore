package org.drappula.arcadeCore.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Location;

import java.io.IOException;
import java.util.Optional;

public class DataConfig extends Config {
    private static DataConfig instance;

    private DataConfig() {
        super("data.yml");
    }

    public static void setup() {
        instance = new DataConfig();
        instance.load();
    }

    public static YamlDocument get() {
        return instance.getDocument();
    }

    public static void setSpawnLocation(Location location) throws IOException {
        get().set("SPAWN_LOCATION", location);
        get().save();
    }

    public static Optional<Location> getSpawnLocation() {
        return get().getAsOptional("SPAWN_LOCATION", Location.class);
    }
}
