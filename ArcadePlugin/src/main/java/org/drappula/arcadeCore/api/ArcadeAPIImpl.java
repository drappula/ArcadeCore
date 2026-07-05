package org.drappula.arcadeCore.api;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.ArcadeAPI;
import org.drappula.arcadeApi.database.UserData;
import org.drappula.arcadeApi.systems.game.Game;
import org.drappula.arcadeApi.systems.game.IMatchManager;
import org.drappula.arcadeApi.systems.game.IParticipant;
import org.drappula.arcadeCore.managers.UserDataManager;
import org.drappula.arcadeCore.managers.game.GameManager;
import org.drappula.arcadeCore.managers.game.MatchManager;

import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class ArcadeAPIImpl implements ArcadeAPI {
    public Optional<UserData> getUserData(UUID uuid) throws SQLException {
        return UserDataManager.get(uuid);
    }

    @Override
    public UserData getOrCreateUserData(UUID uuid, String username) {
        return UserDataManager.getOrCreate(uuid, username);
    }

    @Override
    public void saveUserData(UserData profile) throws SQLException {
        UserDataManager.save(profile);
    }

    @Override @Nullable
    public GameManager getGameManager() {
        return GameManager.get();
    }

    @Override
    public IMatchManager getMatchManager() {
        return MatchManager.get();
    }

    @Override
    public IParticipant getParticipant(Game game, Player player) {
        for (IParticipant participant : GameManager.get().getParticipants().get(game.getId())) {
            if (participant.getPlayer() == player) return participant;
        }
        return null;
    }
    @Override @Deprecated @Nullable
    public IParticipant getParticipant(Player player) {
        for (String gameId : GameManager.get().getGames().keySet()) {
            for (IParticipant participant : GameManager.get().getParticipants().get(gameId)) {
                if (participant.getPlayer() == player) return participant;
            }
        }
        return null;
    }
}
