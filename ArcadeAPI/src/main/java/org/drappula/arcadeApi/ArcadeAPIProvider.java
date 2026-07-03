package org.drappula.arcadeApi;

public final class ArcadeAPIProvider {
    private static ArcadeAPI instance = null;
    public static void register(ArcadeAPI api) {
        if (instance != null) throw new IllegalStateException("API is already registered!");
        instance = api;
    }
    public static void unregister() {
        instance = null;
    }
    public static ArcadeAPI get() {
        if (instance == null) {
            throw new IllegalStateException("ArcadeAPI is not loaded yet!");
        }
        return instance;
    }
}
