package org.drappula.arcadeApi.systems.map;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public interface IMapManager {
    List<IArcadeMap> getMaps(String gameId);
    @Nullable IArcadeMap getMap(String mapId);
    Optional<IArcadeMap> acquireMap(String gameId);
    void releaseMap(IArcadeMap map);
}
