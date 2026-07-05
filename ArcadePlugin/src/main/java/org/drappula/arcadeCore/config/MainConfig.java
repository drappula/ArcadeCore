package org.drappula.arcadeCore.config;

import dev.dejvokep.boostedyaml.YamlDocument;

public class MainConfig extends Config {
    private static MainConfig instance;

    private MainConfig() {
        super("config.yml");
    }

    public static void setup() {
        instance = new MainConfig();
        instance.load();
    }

    public static YamlDocument get() {
        return instance.getDocument();
    }
}
