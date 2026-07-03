package org.drappula.arcadeCore.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.drappula.arcadeCore.config.DataConfig;
import org.drappula.arcadeCore.config.MainConfig;

import java.util.Optional;

public class LobbyListener implements Listener {
    @EventHandler
    public void spawnEvent(PlayerJoinEvent e) {
        Optional<Location> spawnLocation = DataConfig.getSpawnLocation();
        if (MainConfig.get().getBoolean("lobby.teleport-spawn")) {
            spawnLocation.ifPresent(location -> e.getPlayer().teleport(location));
        }
        AttributeInstance maxHealth = e.getPlayer().getAttribute(Attribute.MAX_HEALTH);
        e.getPlayer().setHealth(maxHealth == null ? 20 : maxHealth.getValue());
        e.getPlayer().setGameMode(GameMode.valueOf(MainConfig.get().getString("lobby.gamemode")));
    }
}
