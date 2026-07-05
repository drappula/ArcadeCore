package org.drappula.arcadeApi.systems.game;

import org.drappula.arcadeApi.systems.game.settings.GameEndSettings;

public interface Game {
    default boolean isEnabled() { return true; }
    String getId();
    String getDisplayName();
    int getPlayersRequired();

    default GameEndSettings getGameEndSettings() { return GameEndSettings.DEFAULT; }
}
