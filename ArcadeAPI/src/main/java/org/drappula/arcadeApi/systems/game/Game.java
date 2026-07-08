package org.drappula.arcadeApi.systems.game;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.systems.game.settings.GameEndSettings;
import org.drappula.arcadeApi.systems.map.IArcadeMap;

import java.util.List;

public interface Game {
    default boolean isEnabled() { return true; }
    String getId();
    String getDisplayName();
    int getPlayersRequired();

    /** Lower bound on players needed to start a match. Defaults to the exact {@link #getPlayersRequired()} count. */
    default int getMinPlayers() { return getPlayersRequired(); }
    /** Upper bound on players a single match can hold. Defaults to the exact {@link #getPlayersRequired()} count. */
    default int getMaxPlayers() { return getPlayersRequired(); }

    default GameEndSettings getGameEndSettings() { return GameEndSettings.DEFAULT; }

    /**
     * Games that build their own arena (e.g. procedurally generated) can return a ready-to-use map here
     * instead of relying on {@code MapManager}'s static map pool. Return {@code null} (the default) to use
     * the static pool as usual.
     */
    default IArcadeMap createArena(List<Player> players) { return null; }
}
