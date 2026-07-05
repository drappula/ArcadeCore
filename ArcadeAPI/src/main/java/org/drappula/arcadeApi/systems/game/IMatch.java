package org.drappula.arcadeApi.systems.game;

import org.bukkit.entity.Player;

import java.util.List;

public interface IMatch {
    MatchState getState();
    void setState(MatchState state);
    List<IParticipant> getParticipants();
    List<IParticipant> getEliminatedParticipants();
    List<Player> getSpectatingPlayers();
    Game getGame();
    void end();
}
