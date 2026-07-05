package org.drappula.arcadeCore.managers.game;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.systems.game.MatchState;
import org.drappula.arcadeApi.systems.game.Game;
import org.drappula.arcadeApi.systems.game.IMatch;
import org.drappula.arcadeApi.systems.game.IParticipant;

import java.util.ArrayList;
import java.util.List;

public class Match implements IMatch {
    private MatchState state = MatchState.LOADING;
    private final Game game;
    private final List<IParticipant> participants = new ArrayList<>();
    private final List<IParticipant> eliminatedParticipants = new ArrayList<>();
    private List<IParticipant> winnerParticipants = new ArrayList<>();
    public List<Player> spectatingPlayers = new ArrayList<>();

    public Match(Game game, List<Player> players) {
        this.game = game;
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
