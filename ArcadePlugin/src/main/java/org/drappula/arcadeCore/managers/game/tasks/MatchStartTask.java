package org.drappula.arcadeCore.managers.game.tasks;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.scheduler.BukkitRunnable;
import org.drappula.arcadeApi.systems.game.MatchState;
import org.drappula.arcadeApi.systems.game.IMatch;
import org.drappula.arcadeApi.systems.game.IParticipant;
import org.drappula.arcadeCore.config.MainConfig;
import org.drappula.arcadeCore.config.MessagesConfig;
import org.drappula.arcadeCore.util.MessageUtil;

public class MatchStartTask extends BukkitRunnable {
    private float timeLeft;
    private final IMatch match;
    public MatchStartTask(IMatch match) {
        timeLeft = MainConfig.get().getOptionalFloat("match.start-countdown").orElse(10f);
        this.match = match;
    }
    @Override
    public void run() {
        if (timeLeft == 0) {
            match.setState(MatchState.STARTED);
            for (IParticipant participant : match.getParticipants()) {
                MessageUtil.sendMessage(participant.getPlayer(), MessagesConfig.get().getString("match-started"));
            }
            this.cancel();
            return;
        }
        TagResolver time = TagResolver.resolver(Placeholder.unparsed("time", String.valueOf((int) timeLeft)));
        for (IParticipant participant : match.getParticipants()) {
            MessageUtil.sendMessage(participant.getPlayer(),
                    MessagesConfig.get().getString("countdown-message-" + (timeLeft == 1 ? "singular" : "plural")), time);
        }
        timeLeft--;
    }
}
