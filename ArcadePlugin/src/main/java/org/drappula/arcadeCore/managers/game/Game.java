package org.drappula.arcadeCore.managers.game;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.systems.game.GameState;
import org.drappula.arcadeApi.systems.game.IGame;
import org.drappula.arcadeApi.systems.game.IParticipant;

import java.util.ArrayList;
import java.util.List;

public class Game implements IGame {
    private GameState state = GameState.LOADING;
    private final List<IParticipant> participants = new ArrayList<>();
    private final List<IParticipant> eliminatedParticipants = new ArrayList<>();
    private List<IParticipant> winnerParticipants = new ArrayList<>();

    public Game(List<Player> players) {
        for (Player player : players) {
            participants.add(new Participant(player, this));
        }
    }

    public GameState getState() {
        return state;
    }
    public void setState(GameState state) {
        this.state = state;
    }
    public List<IParticipant> getParticipants() {
        return participants;
    }
    public List<IParticipant> getEliminatedParticipants() {
        return eliminatedParticipants;
    }
    public void addEliminatedParticipant(IParticipant participant) {
        eliminatedParticipants.add(participant);
    }
    public List<IParticipant> getWinnerParticipants() {
        return winnerParticipants;
    }
    public void setWinnerParticipants(List<IParticipant> participants) {
        winnerParticipants = participants;
    }
}
