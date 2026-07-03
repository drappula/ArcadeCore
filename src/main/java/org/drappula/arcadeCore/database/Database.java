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
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS user_profiles (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "username TEXT NOT NULL, " +
                    "first_joined INTEGER NOT NULL, " +
                    "last_joined INTEGER NOT NULL)");
        }
    }

    public static Connection get() {
        return connection;
    }

    public static void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
