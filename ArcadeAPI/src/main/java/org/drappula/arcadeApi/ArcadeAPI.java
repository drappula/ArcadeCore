package org.drappula.arcadeApi;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.database.UserData;
import org.drappula.arcadeApi.systems.game.Game;
import org.drappula.arcadeApi.systems.game.IGameManager;
import org.drappula.arcadeApi.systems.game.IMatchManager;
import org.drappula.arcadeApi.systems.game.IParticipant;

import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public interface ArcadeAPI {
    Optional<UserData> getUserData(UUID uuid) throws SQLException;
    UserData getOrCreateUserData(UUID uuid, String username) throws SQLException;
    void saveUserData(UserData profile) throws SQLException;
    IGameManager getGameManager();
    IMatchManager getMatchManager();
    @Nullable IParticipant getParticipant(Game game, Player player);
    @Deprecated @Nullable IParticipant getParticipant(Player player);
}
