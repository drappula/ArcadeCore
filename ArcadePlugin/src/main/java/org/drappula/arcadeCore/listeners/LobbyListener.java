package org.drappula.arcadeCore.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.drappula.arcadeCore.util.PlayerUtil;

public class LobbyListener implements Listener {
    @EventHandler
    public void spawnEvent(PlayerJoinEvent e) {
        PlayerUtil.sendToLobby(e.getPlayer());
    }
}
