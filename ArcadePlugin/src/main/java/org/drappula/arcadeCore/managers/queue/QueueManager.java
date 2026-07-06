package org.drappula.arcadeCore.managers.queue;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.events.QueueEnterEvent;
import org.drappula.arcadeApi.systems.game.Game;
import org.drappula.arcadeApi.systems.queue.IQueueManager;
import org.drappula.arcadeCore.config.MessagesConfig;
import org.drappula.arcadeCore.managers.game.GameManager;
import org.drappula.arcadeCore.managers.game.MatchManager;
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

    @Override
    public boolean joinQueue(Player player, Game game) {
        leaveQueue(player);
        QueueEnterEvent event = new QueueEnterEvent(player, game);
        event.callEvent();
        if (event.isCancelled()) return false;

        List<Player> queue = queues.computeIfAbsent(game.getId(), k -> new ArrayList<>());
        queue.add(player);
        if (queue.size() >= game.getPlayersRequired()) {
            List<Player> players = new ArrayList<>(queue.subList(0, game.getPlayersRequired()));
            queue.removeAll(players);
            if (MatchManager.get().startMatch(game, players) == null) {
                for (Player queuedPlayer : players) {
                    MessageUtil.sendMessage(queuedPlayer, MessagesConfig.get().getString("map-unavailable"));
                }
            }
        }
        return true;
    }

    @Override
    public void leaveQueue(Player player) {
        for (List<Player> queue : queues.values()) {
            queue.remove(player);
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
        List<Player> players = new ArrayList<>(queue);
        queue.clear();
        if (MatchManager.get().startMatch(game, players) == null) {
            for (Player queuedPlayer : players) {
                MessageUtil.sendMessage(queuedPlayer, MessagesConfig.get().getString("map-unavailable"));
            }
        }
        return true;
    }
}
