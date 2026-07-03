package org.drappula.arcadeCore.managers.impl;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.database.UserData;
import org.drappula.arcadeApi.systems.IProfile;
import org.drappula.arcadeApi.systems.game.IGame;
import org.drappula.arcadeCore.managers.UserDataManager;

public class Profile implements IProfile {
    private final Player player;
    private IGame game;

    public Profile(Player player) {
        this.player = player;
    }

    @Override
    public UserData getUserData() {
        return UserDataManager.getOrCreate(player.getUniqueId(), player.getName());
    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public IGame getGame() {
        return game;
    }
}
