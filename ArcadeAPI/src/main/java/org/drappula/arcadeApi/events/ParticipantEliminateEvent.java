package org.drappula.arcadeApi.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.drappula.arcadeApi.systems.game.IParticipant;
import org.jspecify.annotations.NonNull;

public class ParticipantEliminateEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancelled;
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
    public @NonNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    private final IParticipant participant;
    public IParticipant getParticipant() {
        return participant;
    }

    public ParticipantEliminateEvent(IParticipant participant) {
        this.participant = participant;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
