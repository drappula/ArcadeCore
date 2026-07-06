package org.drappula.arcadeCore.util;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.drappula.arcadeCore.config.DataConfig;
import org.drappula.arcadeCore.config.MainConfig;

import java.util.Optional;

public class PlayerUtil {
    public static void sendToLobby(Player player) {
        Optional<Location> spawnLocation = DataConfig.getSpawnLocation();
        if (MainConfig.get().getBoolean("lobby.teleport-spawn")) spawnLocation.ifPresent(player::teleport);
        resetPlayerState(player, GameMode.valueOf(MainConfig.get().getString("lobby.gamemode")), MainConfig.get().getBoolean("lobby.fly-enabled"));
    }

    public static void teleportToMatch(Player player, Location location) {
        player.teleport(location);
        resetPlayerState(player, GameMode.SURVIVAL, false);
    }

    private static void resetPlayerState(Player player, GameMode gameMode, boolean flying) {
        AttributeInstance maxHealth = player.getAttribute(Attribute.MAX_HEALTH);
        player.setHealth(maxHealth == null ? 20 : maxHealth.getValue());
        player.setGameMode(gameMode);
        player.setFlying(flying);
    }
}
