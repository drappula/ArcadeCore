package org.drappula.arcadeApi.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.drappula.arcadeApi.systems.game.Game;
import org.jetbrains.annotations.NotNull;

public class QueueEnterEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancelled;
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
    @NotNull public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    private final Player player;
    private final Game game;
    public Player getPlayer() {
        return player;
    }
    public Game getGame() {
        return game;
    }

    public QueueEnterEvent(Player player, Game game) {
        this.player = player;
        this.game = game;
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
