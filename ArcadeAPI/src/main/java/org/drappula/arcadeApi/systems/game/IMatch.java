package org.drappula.arcadeApi.systems.game;

import java.util.List;

public interface IMatch {
    public GameState getState();
    public List<IParticipant> getParticipants();
    public IGameType getGameType();
}
