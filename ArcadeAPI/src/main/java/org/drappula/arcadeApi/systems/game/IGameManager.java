package org.drappula.arcadeApi.systems.game;

import javax.annotation.Nullable;

public interface IGameManager {
    void registerGame(Game game);
    void unregisterGame(String id);
    void unregisterGame(Game game);
    @Nullable
    Game getGame(String id);
}
