package org.drappula.arcadeApi.systems.queue;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.systems.game.Game;

import javax.annotation.Nullable;
import java.util.List;

public interface IQueueManager {
    boolean joinQueue(Player player, Game game);
    void leaveQueue(Player player);
    List<Player> getQueue(Game game);
    @Nullable Game getQueuedGame(Player player);
    boolean forceStart(Game game);
}
