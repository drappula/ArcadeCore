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
                "SELECT username, first_joined, last_joined FROM user_profiles WHERE uuid = ?")) {
            stmt.setString(1, uuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new UserProfile(
                        uuid, rs.getString("username"), rs.getLong("first_joined"), rs.getLong("last_joined")));
            }
        }
    }

    public static UserProfile getOrCreate(UUID uuid, String username) throws SQLException {
        Optional<UserProfile> existing = get(uuid);
        long now = System.currentTimeMillis();
        if (existing.isPresent()) {
            UserProfile profile = existing.get();
            profile.setUsername(username);
            profile.setLastJoined(now);
            save(profile);
            return profile;
        }
        UserProfile profile = new UserProfile(uuid, username, now, now);
        save(profile);
        return profile;
    }

    public static void save(UserProfile profile) throws SQLException {
        try (PreparedStatement stmt = Database.get().prepareStatement(
                "INSERT INTO user_profiles (uuid, username, first_joined, last_joined) VALUES (?, ?, ?, ?) " +
                        "ON CONFLICT(uuid) DO UPDATE SET username = excluded.username, last_joined = excluded.last_joined")) {
            stmt.setString(1, profile.getUuid().toString());
            stmt.setString(2, profile.getUsername());
            stmt.setLong(3, profile.getFirstJoined());
            stmt.setLong(4, profile.getLastJoined());
            stmt.executeUpdate();
        }
    }
}
