package org.drappula.arcadeApi.systems.game.settings;

import org.bukkit.GameMode;

import javax.annotation.Nullable;

public class GameEndSettings {
    private EndGameMode gameMode;
    private EndFlyEnabled flyEnabled;
    public EndGameMode getGameMode() {
        return gameMode;
    }
    public void setGameMode(EndGameMode gameMode) {
        this.gameMode = gameMode;
    }
    public EndFlyEnabled isFlyEnabled() {
        return flyEnabled;
    }
    public void setFlyEnabled(boolean flyEnabled) {
        this.flyEnabled = flyEnabled ? EndFlyEnabled.TRUE : EndFlyEnabled.FALSE;
    }
    public void setFlyEnabled(EndFlyEnabled flyEnabled) {
        this.flyEnabled = flyEnabled;
    }
    public static GameEndSettings create(@Nullable GameMode gameMode, @Nullable Boolean flyEnabled) {
        return new GameEndSettings(EndGameMode.parseGameMode(gameMode), EndFlyEnabled.parseFlyEnabled(flyEnabled));
    }
    public static GameEndSettings create(EndGameMode gameMode, EndFlyEnabled flyEnabled) {
        return new GameEndSettings(gameMode, flyEnabled);
    }
    public static GameEndSettings DEFAULT = new GameEndSettings(EndGameMode.DEFAULT, EndFlyEnabled.DEFAULT);
    private GameEndSettings(EndGameMode gameMode, EndFlyEnabled flyEnabled) {
        this.gameMode = gameMode;
        this.flyEnabled = flyEnabled;
    }
}
