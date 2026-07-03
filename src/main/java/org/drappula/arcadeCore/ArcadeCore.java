package org.drappula.arcadeCore;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.drappula.arcadeCore.commands.MainCommand;
import org.drappula.arcadeCore.config.DataConfig;
import org.drappula.arcadeCore.database.Database;

import java.io.IOException;
import java.sql.SQLException;

public final class ArcadeCore extends JavaPlugin {
    private static ArcadeCore instance;
    public static ArcadeCore get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        try {
            new DataConfig();
        } catch (IOException e) {
            getLogger().severe("Failed to initialize data.yml config file:");
            throw new RuntimeException(e);
        }

        try {
            Database.connect();
        } catch (SQLException e) {
            getLogger().severe("Failed to connect to database:");
            throw new RuntimeException(e);
        }

        registerCommands();
    }

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(MainCommand.get());
        });
    }

    @Override
    public void onDisable() {
        try {
            Database.disconnect();
        } catch (SQLException e) {
            getLogger().severe("Failed to close database connection:");
        }
    }
}
