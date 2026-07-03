package org.drappula.arcadeCore.api;

import org.drappula.arcadeApi.ArcadeAPI;
import org.drappula.arcadeApi.database.UserData;
import org.drappula.arcadeCore.managers.UserDataManager;
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
}
