package org.drappula.arcadeApi.systems.game.settings;

import javax.annotation.Nullable;

public enum EndFlyEnabled {
    DEFAULT,
    TRUE,
    FALSE;

    public static EndFlyEnabled parseFlyEnabled(@Nullable Boolean flyEnabled) {
        if (Boolean.TRUE.equals(flyEnabled)) return TRUE;
        if (Boolean.FALSE.equals(flyEnabled)) return FALSE;
        return DEFAULT;
    }
}
