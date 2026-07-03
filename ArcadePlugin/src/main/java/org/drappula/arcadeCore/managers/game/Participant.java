package org.drappula.arcadeCore.managers.game;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.database.UserData;
import org.drappula.arcadeApi.systems.IProfile;
import org.drappula.arcadeApi.systems.game.IGame;
import org.drappula.arcadeApi.systems.game.IParticipant;
import org.drappula.arcadeCore.managers.ProfileManager;
import org.drappula.arcadeCore.managers.UserDataManager;

public class Participant implements IParticipant {
    private final Player player;
    private final IGame game;

    public Participant(Player player, IGame game) {
        this.player = player;
        this.game = game;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public IProfile getProfile() {
        return ProfileManager.getProfile(player);
    }

    @Override
    public UserData getUserData() {
        return UserDataManager.getOrCreate(player.getUniqueId(), player.getName());
    }

    @Override
    public IGame getGame() {
        return game;
    }
}
