package org.drappula.arcadeCore;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.drappula.arcadeApi.ArcadeAPI;
import org.drappula.arcadeApi.ArcadeAPIProvider;
import org.drappula.arcadeCore.api.ArcadeAPIImpl;
import org.drappula.arcadeCore.commands.MainCommand;
import org.drappula.arcadeCore.config.DataConfig;
import org.drappula.arcadeCore.config.MainConfig;
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
            DataConfig.setup();
            MainConfig.setup();
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
        registerAPI();
    }

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> commands.registrar().register(MainCommand.get()));
    }
    private void registerAPI() {
        ArcadeAPI impl = new ArcadeAPIImpl();
        ArcadeAPIProvider.register(impl);
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
