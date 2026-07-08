package org.drappula.arcadeApi.systems;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.database.UserData;
import org.drappula.arcadeApi.systems.game.IMatch;

public interface IProfile {
    UserData getUserData();
    Player getPlayer();
    IMatch getMatch();
    void setMatch(IMatch match);
}
