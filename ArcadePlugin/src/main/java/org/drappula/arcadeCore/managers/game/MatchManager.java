package org.drappula.arcadeCore.managers.game;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.systems.game.GameState;
import org.drappula.arcadeApi.systems.game.IGameType;
import org.drappula.arcadeApi.systems.game.IMatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchManager {
    private static final Map<String, IGameType> gameTypes = new HashMap<>();
    private static final List<IMatch> matches = new ArrayList<>();

    public static void registerGameType(IGameType gameType) {
        gameTypes.put(gameType.getId(), gameType);
    }

    public static IGameType getGameType(String id) {
        return gameTypes.get(id);
    }

    public static List<IMatch> getMatches() {
        return matches;
    }

    public static Match startMatch(String gameTypeId, List<Player> players) {
        IGameType gameType = gameTypes.get(gameTypeId);
        if (gameType == null) {
            throw new IllegalArgumentException("No game type registered with id " + gameTypeId);
        }
        Match match = new Match(gameType, players);
        match.setState(GameState.STARTING);
        matches.add(match);
        gameType.onMatchStart(match);
        match.setState(GameState.STARTED);
        return match;
    }

    public static void endMatch(Match match) {
        match.setState(GameState.ENDING);
        match.getGameType().onMatchEnd(match);
        match.setState(GameState.ENDED);
        matches.remove(match);
    }
}
