package org.drappula.arcadeCore.api;

import org.drappula.arcadeApi.ArcadeAPI;
import org.drappula.arcadeCore.database.UserProfileManager;
import org.drappula.arcadeApi.database.UserProfile;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class ArcadeAPIImpl implements ArcadeAPI {
    @Override
    public Optional<UserProfile> getProfile(UUID uuid) throws SQLException {
        return UserProfileManager.get(uuid);
    }

    @Override
    public UserProfile getOrCreateProfile(UUID uuid, String username) throws SQLException {
        return UserProfileManager.getOrCreate(uuid, username);
    }

    @Override
    public void saveProfile(UserProfile profile) throws SQLException {
        UserProfileManager.save(profile);
    }
}
