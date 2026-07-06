package org.drappula.arcadeCore.managers.map;

import org.bukkit.Location;
import org.drappula.arcadeApi.systems.map.IArcadeMap;
import org.drappula.arcadeApi.systems.map.IMapManager;
import org.drappula.arcadeCore.ArcadeCore;
import org.drappula.arcadeCore.database.MapDataManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MapManager implements IMapManager {
    private static MapManager instance;
    public static MapManager get() {
        if (instance == null) instance = new MapManager();
        return instance;
    }

    private final List<ArcadeMap> maps = new ArrayList<>();

    public void load() {
        maps.clear();
        try {
            maps.addAll(MapDataManager.loadAll());
        } catch (SQLException e) {
            ArcadeCore.get().getSLF4JLogger().error("Failed to load maps from the database", e);
        }
    }

    public List<IArcadeMap> getMaps(String gameId) {
        List<IArcadeMap> result = new ArrayList<>();
        for (ArcadeMap map : maps) {
            if (map.getGameId().equalsIgnoreCase(gameId)) result.add(map);
        }
        return result;
    }

    public ArcadeMap getMap(String mapId) {
        for (ArcadeMap map : maps) {
            if (map.getId().equalsIgnoreCase(mapId)) return map;
        }
        return null;
    }

    public Optional<IArcadeMap> acquireMap(String gameId) {
        for (ArcadeMap map : maps) {
            if (map.getGameId().equalsIgnoreCase(gameId) && map.isEnabled() && !map.isInUse() && !map.getSpawnPoints().isEmpty()) {
                map.setInUse(true);
                try {
                    MapDataManager.setInUse(map.getId(), true);
                } catch (SQLException e) {
                    ArcadeCore.get().getSLF4JLogger().error("Failed to persist in-use flag for map {}", map.getId(), e);
                }
                return Optional.of(map);
            }
        }
        return Optional.empty();
    }

    public void releaseMap(IArcadeMap map) {
        if (map == null) return;
        ArcadeMap tracked = getMap(map.getId());
        if (tracked == null) return;
        tracked.setInUse(false);
        try {
            MapDataManager.setInUse(tracked.getId(), false);
        } catch (SQLException e) {
            ArcadeCore.get().getSLF4JLogger().error("Failed to persist in-use flag for map {}", tracked.getId(), e);
        }
    }

    public void releaseAll() {
        for (ArcadeMap map : maps) map.setInUse(false);
        try {
            MapDataManager.releaseAll();
        } catch (SQLException e) {
            ArcadeCore.get().getSLF4JLogger().error("Failed to release maps in the database", e);
        }
    }

    public void createMap(String mapId, String gameId, String displayName, String world) throws SQLException {
        MapDataManager.create(mapId, gameId, displayName, world);
        maps.add(new ArcadeMap(mapId, gameId, displayName, world, true, false, new ArrayList<>()));
    }

    public void addSpawn(String mapId, Location location) throws SQLException {
        ArcadeMap map = getMap(mapId);
        if (map == null) return;
        MapDataManager.addSpawn(mapId, map.getSpawnPoints().size(), location);
        map.getSpawnPoints().add(location);
    }

    public void setEnabled(String mapId, boolean enabled) throws SQLException {
        ArcadeMap map = getMap(mapId);
        if (map == null) return;
        MapDataManager.setEnabled(mapId, enabled);
        map.setEnabled(enabled);
    }

    public void deleteMap(String mapId) throws SQLException {
        MapDataManager.delete(mapId);
        maps.removeIf(m -> m.getId().equalsIgnoreCase(mapId));
    }
}
