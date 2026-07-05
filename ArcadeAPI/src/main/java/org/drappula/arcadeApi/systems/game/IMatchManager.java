package org.drappula.arcadeApi.systems.game;

import java.util.List;

public interface IMatchManager {
    List<IMatch> getMatchesForGame(Game game);
    List<IMatch> getMatchesForGame(String gameId);
}
