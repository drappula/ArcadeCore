package org.drappula.arcadeCore.managers.game;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.drappula.arcadeApi.events.MatchEndEvent;
import org.drappula.arcadeApi.events.MatchStartEvent;
import org.drappula.arcadeApi.systems.game.*;
import org.drappula.arcadeApi.systems.game.settings.EndFlyEnabled;
import org.drappula.arcadeApi.systems.game.settings.EndGameMode;
import org.drappula.arcadeApi.systems.game.settings.GameEndSettings;
import org.drappula.arcadeCore.ArcadeCore;
import org.drappula.arcadeCore.config.MainConfig;
import org.drappula.arcadeCore.config.MessagesConfig;
import org.drappula.arcadeCore.managers.game.tasks.MatchEndTask;
import org.drappula.arcadeCore.managers.game.tasks.MatchStartTask;
import org.drappula.arcadeCore.util.MessageUtil;

import java.util.List;
import java.util.Map;

public class MatchManager implements IMatchManager {
    private static MatchManager instance;
    public static MatchManager get() {
        if (instance == null) instance = new MatchManager();
        return instance;
    }

    public Map<String, List<IMatch>> getMatches() {
        return GameManager.get().getMatches();
    }
    public List<IMatch> getMatchesForGame(String gameId) {
        return GameManager.get().getMatches().get(gameId);
    }
    public List<IMatch> getMatchesForGame(Game game) {
        return getMatchesForGame(game.getId());
    }

    public Match startMatch(Game game, List<Player> players) {
        if (!game.isEnabled()) {
            throw new IllegalStateException("Tried to start a match for a disabled game! (" + game.getId() + ")");
        }
        Match match = new Match(game, players);
        GameManager.get().populateMatch(match);
        MatchStartEvent startEvent = new MatchStartEvent(match);
        startEvent.callEvent();
        if (startEvent.isCancelled()) {
            GameManager.get().depopulateMatch(match);
            return null;
        }
        match.setState(MatchState.STARTING);
        getMatches().get(match.getGame().getId()).add(match);
        new MatchStartTask(match).runTaskTimer(ArcadeCore.get(), 0, 20);
        return match;
    }

    public void endMatch(IMatch match) {
        match.setState(MatchState.ENDING);
        new MatchEndTask(match).runTaskTimer(ArcadeCore.get(), 0, 20);
        new MatchEndEvent(match).callEvent();
    }

    public void eliminateParticipant(IParticipant participant) {
        participant.getMatch().getParticipants().remove(participant);
        participant.getMatch().getEliminatedParticipants().add(participant);
        participant.getMatch().getSpectatingPlayers().add(participant.getPlayer());
        GameManager.get().getParticipants().get(participant.getMatch().getGame().getId()).remove(participant);
        GameEndSettings settings = participant.getMatch().getGame().getGameEndSettings();
        GameMode defaultGameMode = GameMode.valueOf(MainConfig.get().getString("match.end.gamemode"));
        boolean defaultFlyEnabled = MainConfig.get().getBoolean("match.end.fly-enabled");
        participant.getPlayer().setGameMode(settings.getGameMode() == EndGameMode.DEFAULT ? defaultGameMode : settings.getGameMode().getGameMode());
        participant.getPlayer().setFlying(settings.isFlyEnabled() == EndFlyEnabled.DEFAULT ? defaultFlyEnabled : (settings.isFlyEnabled() == EndFlyEnabled.TRUE));

        TagResolver eliminated = TagResolver.resolver(Placeholder.unparsed("participant", participant.getPlayer().getName()));
        for (IParticipant remaining : participant.getMatch().getParticipants()) {
            MessageUtil.sendMessage(remaining.getPlayer(), MessagesConfig.get().getString("participant-eliminated"), eliminated);
        }
    }
}
