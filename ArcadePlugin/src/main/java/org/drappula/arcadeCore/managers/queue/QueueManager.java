package org.drappula.arcadeCore.managers.queue;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.events.QueueEnterEvent;
import org.drappula.arcadeApi.systems.game.Game;
import org.drappula.arcadeApi.systems.queue.IQueueManager;
import org.drappula.arcadeCore.ArcadeCore;
import org.drappula.arcadeCore.config.MessagesConfig;
import org.drappula.arcadeCore.managers.game.GameManager;
import org.drappula.arcadeCore.managers.game.MatchManager;
import org.drappula.arcadeCore.managers.queue.tasks.QueueCountdownTask;
import org.drappula.arcadeCore.util.MessageUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueueManager implements IQueueManager {
    private static QueueManager instance;
    public static QueueManager get() {
        if (instance == null) instance = new QueueManager();
        return instance;
    }

    private final Map<String, List<Player>> queues = new HashMap<>();
    private final Map<String, QueueCountdownTask> countdowns = new HashMap<>();

    @Override
    public boolean joinQueue(Player player, Game game) {
        leaveQueue(player);
        QueueEnterEvent event = new QueueEnterEvent(player, game);
        event.callEvent();
        if (event.isCancelled()) return false;

        List<Player> queue = queues.computeIfAbsent(game.getId(), k -> new ArrayList<>());
        queue.add(player);
        if (queue.size() >= game.getMaxPlayers()) {
            startQueuedMatch(game);
        } else if (queue.size() >= game.getMinPlayers() && !countdowns.containsKey(game.getId())) {
            QueueCountdownTask task = new QueueCountdownTask(game);
            task.runTaskTimer(ArcadeCore.get(), 0, 20);
            countdowns.put(game.getId(), task);
        }
        return true;
    }

    @Override
    public void leaveQueue(Player player) {
        for (Map.Entry<String, List<Player>> entry : queues.entrySet()) {
            entry.getValue().remove(player);
            Game game = GameManager.get().getGame(entry.getKey());
            if (game != null && entry.getValue().size() < game.getMinPlayers()) {
                cancelCountdown(game);
            }
        }
    }

    @Override
    public List<Player> getQueue(Game game) {
        return queues.getOrDefault(game.getId(), List.of());
    }

    @Override
    @Nullable
    public Game getQueuedGame(Player player) {
        for (Map.Entry<String, List<Player>> entry : queues.entrySet()) {
            if (entry.getValue().contains(player)) {
                return GameManager.get().getGame(entry.getKey());
            }
        }
        return null;
    }

    @Override
    public boolean forceStart(Game game) {
        List<Player> queue = queues.get(game.getId());
        if (queue == null || queue.isEmpty()) return false;
        startQueuedMatch(game);
        return true;
    }

    /** Cancels this game's pending queue countdown, if any (e.g. the queue dropped below the minimum). */
    public void cancelCountdown(Game game) {
        QueueCountdownTask task = countdowns.remove(game.getId());
        if (task != null) task.cancel();
    }

    /** Slices up to {@code getMaxPlayers()} players off this game's queue and starts a match with them. */
    public void startQueuedMatch(Game game) {
        cancelCountdown(game);
        List<Player> queue = queues.get(game.getId());
        if (queue == null || queue.isEmpty()) return;
        int count = Math.min(queue.size(), game.getMaxPlayers());
        List<Player> players = new ArrayList<>(queue.subList(0, count));
        queue.removeAll(players);
        if (MatchManager.get().startMatch(game, players) == null) {
            for (Player queuedPlayer : players) {
                MessageUtil.sendMessage(queuedPlayer, MessagesConfig.get().getString("map-unavailable"));
            }
        }
    }
}
