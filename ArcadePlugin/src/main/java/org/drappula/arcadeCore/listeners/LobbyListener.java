package org.drappula.arcadeCore.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.drappula.arcadeCore.managers.ProfileManager;
import org.drappula.arcadeCore.managers.impl.Profile;
import org.drappula.arcadeCore.util.PlayerUtil;

public class LobbyListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        ProfileManager.registerProfile(new Profile(e.getPlayer()));
        PlayerUtil.sendToLobby(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        ProfileManager.removeProfile(e.getPlayer());
    }
}
