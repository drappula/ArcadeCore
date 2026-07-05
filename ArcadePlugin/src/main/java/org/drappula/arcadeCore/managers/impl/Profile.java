package org.drappula.arcadeCore.managers.impl;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.database.UserData;
import org.drappula.arcadeApi.systems.IProfile;
import org.drappula.arcadeApi.systems.game.IMatch;
import org.drappula.arcadeCore.managers.UserDataManager;

public class Profile implements IProfile {
    private final Player player;
    private IMatch match;

    public Profile(Player player) {
        this.player = player;
    }

    @Override
    public UserData getUserData() {
        return UserDataManager.getOrCreate(player.getUniqueId(), player.getName());
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public IMatch getMatch() {
        return match;
    }

    @Override
    public void setMatch(IMatch match) {
        this.match = match;
    }
}
