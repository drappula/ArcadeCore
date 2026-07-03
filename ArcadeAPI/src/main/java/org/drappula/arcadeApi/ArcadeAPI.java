package org.drappula.arcadeApi;

import org.drappula.arcadeApi.database.UserProfile;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public interface ArcadeAPI {
    Optional<UserProfile> getProfile(UUID uuid) throws SQLException;
    UserProfile getOrCreateProfile(UUID uuid, String username) throws SQLException;
    void saveProfile(UserProfile profile) throws SQLException;
}
