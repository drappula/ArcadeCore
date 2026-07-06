package org.drappula.arcadeCore.managers.map;

import org.bukkit.Location;
import org.drappula.arcadeApi.systems.map.IArcadeMap;

import java.util.List;

public class ArcadeMap implements IArcadeMap {
    private final String id;
    private final String gameId;
    private final String displayName;
    private final String worldName;
    private boolean enabled;
    private boolean inUse;
    private final List<Location> spawnPoints;

    public ArcadeMap(String id, String gameId, String displayName, String worldName, boolean enabled, boolean inUse, List<Location> spawnPoints) {
        this.id = id;
        this.gameId = gameId;
        this.displayName = displayName;
        this.worldName = worldName;
        this.enabled = enabled;
        this.inUse = inUse;
        this.spawnPoints = spawnPoints;
    }

    public String getId() {
        return id;
    }
    public String getGameId() {
        return gameId;
    }
    public String getDisplayName() {
        return displayName;
    }
    public String getWorldName() {
        return worldName;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public boolean isInUse() {
        return inUse;
    }
    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }
    public List<Location> getSpawnPoints() {
        return spawnPoints;
    }
}
