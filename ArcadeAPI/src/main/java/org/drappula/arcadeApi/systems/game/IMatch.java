package org.drappula.arcadeApi.systems.game;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.systems.map.IArcadeMap;

import javax.annotation.Nullable;
import java.util.List;

public interface IMatch {
    MatchState getState();
    void setState(MatchState state);
    List<IParticipant> getParticipants();
    List<IParticipant> getEliminatedParticipants();
    List<Player> getSpectatingPlayers();
    Game getGame();
    @Nullable IArcadeMap getMap();
    void end();
}
