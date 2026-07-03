package org.drappula.arcadeCore;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.drappula.arcadeCore.commands.MainCommand;
import org.drappula.arcadeCore.config.DataConfig;

import java.io.IOException;

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

        registerCommands();
    }

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(MainCommand.get());
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
