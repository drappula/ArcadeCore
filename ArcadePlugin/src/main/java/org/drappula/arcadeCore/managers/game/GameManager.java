package org.drappula.arcadeCore.managers.game;

import org.drappula.arcadeApi.systems.game.Game;
import org.drappula.arcadeApi.systems.game.IGameManager;
import org.drappula.arcadeApi.systems.game.IMatch;
import org.drappula.arcadeApi.systems.game.IParticipant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager implements IGameManager {
    private static GameManager instance;
    public static GameManager get() {
        if (instance == null) instance = new GameManager();
        return instance;
    }

    private Map<String, Game> games = new HashMap<>();
    private Map<String, List<IMatch>> matches = new HashMap<>();
    private Map<String, List<IParticipant>> participants = new HashMap<>();

    public Map<String, Game> getGames() {
        return games;
    }
    public Map<String, List<IMatch>> getMatches() {
        return matches;
    }
    public Map<String, List<IParticipant>> getParticipants() {
        return participants;
    }

    public void registerGame(Game game) {
        games.put(game.getId(), game);
    }
    public void unregisterGame(Game game) {
        this.unregisterGame(game.getId());
    }
    public Game getGame(String id) {
        return games.get(id);
    }
    public void unregisterGame(String id) {
        games.remove(id);
    }
    public void populateMatch(IMatch match) {
        String gameId = match.getGame().getId();
        matches.computeIfAbsent(gameId, k -> new ArrayList<>());
        participants.computeIfAbsent(gameId, k -> new ArrayList<>());
        matches.get(gameId).add(match);
        participants.get(gameId).addAll(match.getParticipants());
    }
    public void depopulateMatch(IMatch match) {
        String gameId = match.getGame().getId();
        matches.computeIfAbsent(gameId, k -> new ArrayList<>());
        participants.computeIfAbsent(gameId, k -> new ArrayList<>());
        matches.get(gameId).remove(match);
        participants.get(gameId).removeAll(match.getParticipants());
    }
    public void reload() { // TODO: use it
        games = new HashMap<>();
        matches = new HashMap<>();
        participants = new HashMap<>();
    }
}
