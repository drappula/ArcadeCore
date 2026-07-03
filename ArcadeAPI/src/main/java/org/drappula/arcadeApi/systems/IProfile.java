package org.drappula.arcadeApi.systems;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.database.UserData;
import org.drappula.arcadeApi.systems.game.IGame;

public interface IProfile {
    UserData getUserData();
    Player getPlayer();
    IGame getGame();
}
