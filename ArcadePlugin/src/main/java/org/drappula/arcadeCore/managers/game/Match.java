package org.drappula.arcadeCore.managers.game;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.systems.game.MatchState;
import org.drappula.arcadeApi.systems.game.Game;
import org.drappula.arcadeApi.systems.game.IMatch;
import org.drappula.arcadeApi.systems.game.IParticipant;
import org.drappula.arcadeApi.systems.map.IArcadeMap;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Match implements IMatch {
    private MatchState state = MatchState.LOADING;
    private final Game game;
    private final IArcadeMap map;
    private final List<IParticipant> participants = new ArrayList<>();
    private final List<IParticipant> eliminatedParticipants = new ArrayList<>();
    private List<IParticipant> winnerParticipants = new ArrayList<>();
    public List<Player> spectatingPlayers = new ArrayList<>();

    public Match(Game game, List<Player> players, @Nullable IArcadeMap map) {
        this.game = game;
        this.map = map;
        for (Player player : players) {
            participants.add(new Participant(player, this));
        }
    }

    public MatchState getState() {
        return state;
    }
    public void setState(MatchState state) {
        this.state = state;
    }
    public Game getGame() {
        return game;
    }
    @Nullable
    public IArcadeMap getMap() {
        return map;
    }
    public List<IParticipant> getParticipants() {
        return participants;
    }
    public List<IParticipant> getEliminatedParticipants() {
        return eliminatedParticipants;
    }
    public List<Player> getSpectatingPlayers() {
        return spectatingPlayers;
    }

    public void end() {
        MatchManager.get().endMatch(this);
    }

    public List<IParticipant> getWinnerParticipants() {
        return winnerParticipants;
    }
    public void setWinnerParticipants(List<IParticipant> participants) {
        winnerParticipants = participants;
    }
}
