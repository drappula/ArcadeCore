package org.drappula.arcadeCore;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.drappula.arcadeApi.ArcadeAPI;
import org.drappula.arcadeApi.ArcadeAPIProvider;
import org.drappula.arcadeCore.api.ArcadeAPIImpl;
import org.drappula.arcadeCore.commands.MainCommand;
import org.drappula.arcadeCore.config.DataConfig;
import org.drappula.arcadeCore.config.MainConfig;
import org.drappula.arcadeCore.config.MessagesConfig;
import org.drappula.arcadeCore.database.Database;
import org.drappula.arcadeCore.listeners.LobbyListener;
import org.drappula.arcadeCore.managers.game.GameManager;
import org.drappula.arcadeCore.managers.map.MapManager;

import java.sql.SQLException;

public final class ArcadeCore extends JavaPlugin {
    private static ArcadeCore instance;
    public static ArcadeCore get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        setupConfig();
        connectDatabase();
        MapManager.get().load();
        registerCommands();
        registerListeners();
        registerAPI();
    }
    private void setupConfig() {
        DataConfig.setup();
        MainConfig.setup();
        MessagesConfig.setup();
    }
    private void connectDatabase() {
        try {
            Database.connect();
        } catch (SQLException e) {
            getLogger().severe("Failed to connect to database:");
            throw new RuntimeException(e);
        }
    }
    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> commands.registrar().register(MainCommand.get()));
    }
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new LobbyListener(), this);
    }
    private void registerAPI() {
        ArcadeAPI impl = new ArcadeAPIImpl();
        ArcadeAPIProvider.register(impl);
    }

    @Override
    public void onDisable() {
        GameManager.get().reload();
        MapManager.get().releaseAll();
        try {
            Database.disconnect();
        } catch (SQLException e) {
            getLogger().severe("Failed to close database connection:");
        }
    }
}
