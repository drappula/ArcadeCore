package org.drappula.arcadeCore.config;

import dev.dejvokep.boostedyaml.YamlDocument;

public class MessagesConfig extends Config {
    private static MessagesConfig instance;

    private MessagesConfig() {
        super("messages.yml");
    }

    public static void setup() {
        instance = new MessagesConfig();
        instance.load();
    }

    public static YamlDocument get() {
        return instance.getDocument();
    }
}
