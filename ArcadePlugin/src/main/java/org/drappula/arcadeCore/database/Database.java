package org.drappula.arcadeCore.database;

import org.drappula.arcadeCore.ArcadeCore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static Connection connection;

    public static void connect() throws SQLException {
        ArcadeCore.get().getDataFolder().mkdirs();
        connection = DriverManager.getConnection(
                "jdbc:sqlite:" + ArcadeCore.get().getDataFolder().getAbsolutePath() + "/database.db");
        try (Statement pragmaStmt = connection.createStatement()) {
            pragmaStmt.execute("PRAGMA foreign_keys = ON;");
        }
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS user_profiles (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "username TEXT NOT NULL)");
            stmt.execute("CREATE TABLE IF NOT EXISTS games (" +
                    "game_id TEXT PRIMARY KEY)");
            stmt.execute("CREATE TABLE IF NOT EXISTS game_stats (" +
                    "uuid TEXT, " +
                    "game_id TEXT, " +
                    "points INT, " +
                    "wins INT, " +
                    "losses INT, " +
                    "other_stats JSON, " +
                    "PRIMARY KEY (uuid, game_id), " +
                    "FOREIGN KEY (uuid) REFERENCES user_profiles(uuid) ON DELETE CASCADE, " +
                    "FOREIGN KEY (game_id) REFERENCES games(game_id) ON DELETE CASCADE)");
        }
    }

    public static Connection get() {
        return connection;
    }

    public static void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
