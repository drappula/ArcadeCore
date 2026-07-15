package org.drappula.arcadeCore.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.drappula.arcadeApi.systems.game.IMatch;
import org.drappula.arcadeApi.systems.game.IParticipant;
import org.drappula.arcadeCore.managers.ProfileManager;
import org.drappula.arcadeCore.managers.game.GameManager;
import org.drappula.arcadeCore.managers.impl.Profile;
import org.drappula.arcadeCore.managers.queue.QueueManager;
import org.drappula.arcadeCore.util.PlayerUtil;

import java.util.List;

public class LobbyListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        ProfileManager.registerProfile(new Profile(e.getPlayer()));
        PlayerUtil.sendToLobby(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        QueueManager.get().leaveQueue(e.getPlayer());
        // Eliminate the player from any match they're still alive in (copy: eliminate() mutates the list)
        for (String gameId : GameManager.get().getGames().keySet()) {
            for (IParticipant participant : List.copyOf(GameManager.get().getParticipants().getOrDefault(gameId, List.of()))) {
                if (participant.getPlayer() == e.getPlayer() && !participant.isEliminated()) {
                    participant.eliminate();
                }
            }
        }
        // Drop them from spectator lists so match-end teardown doesn't act on an offline player
        for (List<IMatch> matches : GameManager.get().getMatches().values()) {
            for (IMatch match : matches) {
                match.getSpectatingPlayers().remove(e.getPlayer());
            }
        }
        ProfileManager.removeProfile(e.getPlayer());
    }
}
