package org.drappula.arcadeApi.systems.game;

public interface IGameType {
    String getId();
    void onMatchStart(IMatch match);
    void onMatchEnd(IMatch match);
}
