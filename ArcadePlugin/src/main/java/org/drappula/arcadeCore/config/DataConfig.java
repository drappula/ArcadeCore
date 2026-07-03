package org.drappula.arcadeCore.config;

import org.bukkit.Location;

import java.io.IOException;
import java.util.Optional;

public class DataConfig extends Config {
    public DataConfig() {
        super("data.yml");
    }
    public static void setSpawnLocation(Location location) throws IOException {
        config.set("SPAWN_LOCATION", location);
        config.save();
    }
    public static Optional<Location> getSpawnLocation() {
        return config.getAsOptional("SPAWN_LOCATION", Location.class);
    }
}
