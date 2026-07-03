package org.drappula.arcadeApi;

import org.drappula.arcadeApi.database.UserData;
import org.drappula.arcadeApi.systems.game.IGameType;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public interface ArcadeAPI {
    Optional<UserData> getUserData(UUID uuid) throws SQLException;
    UserData getOrCreateUserData(UUID uuid, String username) throws SQLException;
    void saveUserData(UserData profile) throws SQLException;
    void registerGameType(IGameType gameType);
}
