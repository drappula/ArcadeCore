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
        if (game.getId().isBlank() || game.getId().contains(" ")) throw new IllegalArgumentException("Tried to register game with invalid ID (" + game.getId() + ")");
        games.put(game.getId().toLowerCase(), game);
    }
    public void unregisterGame(Game game) {
        this.unregisterGame(game.getId());
    }
    public Game getGame(String id) {
        // registerGame lowercases the key, so lookups must too
        return games.get(id.toLowerCase());
    }
    public void unregisterGame(String id) {
        games.remove(id.toLowerCase());
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
    public void reload() {
        games = new HashMap<>();
        matches = new HashMap<>();
        participants = new HashMap<>();
    }
}
