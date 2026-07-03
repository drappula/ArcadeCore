package org.drappula.arcadeApi.database;

import java.util.UUID;

public class UserProfile {
    private final UUID uuid;
    private String username;

    public UserProfile(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
