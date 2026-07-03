package org.drappula.arcadeCore.database;

import org.drappula.arcadeApi.database.UserProfile;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserProfileManager {
    public static Optional<UserProfile> get(UUID uuid) throws SQLException {
        try (PreparedStatement stmt = Database.get().prepareStatement(
                "SELECT username FROM user_profiles WHERE uuid = ?")) {
            stmt.setString(1, uuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new UserProfile(uuid, rs.getString("username")));
            }
        }
    }

    public static UserProfile getOrCreate(UUID uuid, String username) throws SQLException {
        Optional<UserProfile> existing = get(uuid);
        if (existing.isPresent()) {
            UserProfile profile = existing.get();
            profile.setUsername(username);
            save(profile);
            return profile;
        }
        UserProfile profile = new UserProfile(uuid, username);
        save(profile);
        return profile;
    }

    public static void save(UserProfile profile) throws SQLException {
        try (PreparedStatement stmt = Database.get().prepareStatement(
                "INSERT INTO user_profiles (uuid, username) VALUES (?, ?, ?, ?) " +
                        "ON CONFLICT(uuid) DO UPDATE SET username = excluded.username")) {
            stmt.setString(1, profile.getUuid().toString());
            stmt.setString(2, profile.getUsername());
            stmt.executeUpdate();
        }
    }
}
