package org.drappula.arcadeApi.systems.game;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.database.UserData;
import org.drappula.arcadeApi.systems.IProfile;

public interface IParticipant {
    Player getPlayer();
    IProfile getProfile();
    UserData getUserData();
    IMatch getMatch();
    boolean isEliminated();

    void eliminate();
}
