package org.drappula.arcadeCore.database;

import java.util.UUID;

public class UserProfile {
    private final UUID uuid;
    private String username;
    private final long firstJoined;
    private long lastJoined;

    public UserProfile(UUID uuid, String username, long firstJoined, long lastJoined) {
        this.uuid = uuid;
        this.username = username;
        this.firstJoined = firstJoined;
        this.lastJoined = lastJoined;
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

    public long getFirstJoined() {
        return firstJoined;
    }

    public long getLastJoined() {
        return lastJoined;
    }

    public void setLastJoined(long lastJoined) {
        this.lastJoined = lastJoined;
    }
}
