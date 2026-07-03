package org.drappula.arcadeCore.api;

import org.drappula.arcadeCore.database.UserProfile;
import org.drappula.arcadeCore.database.UserProfileManager;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class ArcadeAPI {
    private static final ArcadeAPI instance = new ArcadeAPI();

    public static ArcadeAPI get() {
        return instance;
    }

    public Optional<UserProfile> getProfile(UUID uuid) throws SQLException {
        return UserProfileManager.get(uuid);
    }

    public UserProfile getOrCreateProfile(UUID uuid, String username) throws SQLException {
        return UserProfileManager.getOrCreate(uuid, username);
    }

    public void saveProfile(UserProfile profile) throws SQLException {
        UserProfileManager.save(profile);
    }
}
