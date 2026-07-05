package org.drappula.arcadeCore.managers.game;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.database.UserData;
import org.drappula.arcadeApi.events.ParticipantEliminateEvent;
import org.drappula.arcadeApi.systems.IProfile;
import org.drappula.arcadeApi.systems.game.IMatch;
import org.drappula.arcadeApi.systems.game.IParticipant;
import org.drappula.arcadeApi.systems.game.MatchState;
import org.drappula.arcadeCore.managers.ProfileManager;
import org.drappula.arcadeCore.managers.UserDataManager;

public class Participant implements IParticipant {
    private final Player player;
    private final IMatch match;
    private boolean eliminated = false;

    public Participant(Player player, IMatch match) {
        this.player = player;
        this.match = match;
    }

    @Override
    public IProfile getProfile() {
        return ProfileManager.getProfile(player);
    }

    @Override
    public UserData getUserData() {
        return UserDataManager.getOrCreate(player.getUniqueId(), player.getName());
    }

    @Override
    public IMatch getMatch() {
        return match;
    }

    @Override
    public boolean isEliminated() {
        return eliminated;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void eliminate() {
        if (getMatch().getState() == MatchState.ENDING || getMatch().getState() == MatchState.ENDED)
            throw new IllegalStateException("Tried to eliminate player when match was ending/ended.");
        ParticipantEliminateEvent event = new ParticipantEliminateEvent(this);
        event.callEvent();
        if (event.isCancelled()) return;
        eliminated = true;
        MatchManager.get().eliminateParticipant(this);
    }
}