package org.drappula.arcadeApi.systems.game;

import java.util.List;

public interface IGame {
    public GameState getState();
    public List<IParticipant> getParticipants();
}
