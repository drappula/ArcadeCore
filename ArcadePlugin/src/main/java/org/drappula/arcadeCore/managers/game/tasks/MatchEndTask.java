package org.drappula.arcadeCore.managers.game.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.drappula.arcadeApi.systems.game.MatchState;
import org.drappula.arcadeApi.systems.game.IMatch;
import org.drappula.arcadeApi.systems.game.IParticipant;
import org.drappula.arcadeCore.config.MessagesConfig;
import org.drappula.arcadeCore.managers.game.GameManager;
import org.drappula.arcadeCore.managers.game.MatchManager;
import org.drappula.arcadeCore.managers.map.MapManager;
import org.drappula.arcadeCore.util.MessageUtil;
import org.drappula.arcadeCore.util.PlayerUtil;

public class MatchEndTask extends BukkitRunnable {
    private int time = 5;
    private final IMatch match;
    public MatchEndTask(IMatch match) {
        this.match = match;
    }
    @Override
    public void run() {
        if (time == 0) {
            match.setState(MatchState.ENDED);
            GameManager.get().depopulateMatch(match);
            MapManager.get().releaseMap(match.getMap());
            for (Player player : match.getSpectatingPlayers()) {
                MessageUtil.sendMessage(player, MessagesConfig.get().getString("match-ended"));
                PlayerUtil.sendToLobby(player);
            }
            this.cancel();
            return;
        }
        if (time == 5) {
            for (IParticipant participant : match.getParticipants()) {
                MatchManager.get().eliminateParticipant(participant);
            }
        }
        time--;
    }
}
