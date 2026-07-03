package org.drappula.arcadeCore.api;

import org.drappula.arcadeApi.ArcadeAPI;
import org.drappula.arcadeApi.database.UserData;
import org.drappula.arcadeApi.systems.game.IGameType;
import org.drappula.arcadeCore.managers.UserDataManager;
import org.drappula.arcadeCore.managers.game.MatchManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class ArcadeAPIImpl implements ArcadeAPI {
    public Optional<UserData> getUserData(UUID uuid) throws SQLException {
        return UserDataManager.get(uuid);
    }

    @Override
    public UserData getOrCreateUserData(UUID uuid, String username) throws SQLException {
        return UserDataManager.getOrCreate(uuid, username);
    }

    @Override
    public void saveUserData(UserData profile) throws SQLException {
        UserDataManager.save(profile);
    }

    @Override
    public void registerGameType(IGameType gameType) {
        MatchManager.registerGameType(gameType);
    }
}
