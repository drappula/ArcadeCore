package org.drappula.arcadeApi.systems.game;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.database.UserData;
import org.drappula.arcadeApi.systems.IProfile;

public interface IParticipant {
    public Player getPlayer();
    public IProfile getProfile();
    public UserData getUserData();
    public IGame getGame();
}
