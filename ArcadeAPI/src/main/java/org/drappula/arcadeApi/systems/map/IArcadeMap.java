package org.drappula.arcadeApi.systems.map;

import org.bukkit.Location;

import java.util.List;

public interface IArcadeMap {
    String getId();
    String getGameId();
    String getDisplayName();
    String getWorldName();
    boolean isEnabled();
    boolean isInUse();
    List<Location> getSpawnPoints();
}
