package com.github.elic0de.h1.database;

import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connects to and uses a SQLite database
 */
@SuppressWarnings("PMD.CommentRequired")
public class SQLite extends Database {
    private final String dbLocation;

    /**
     * Creates a new SQLite instance
     *
     * @param plugin     Plugin instance
     * @param dbLocation Location of the Database (Must end in .db)
     */
    public SQLite(final H1Plugin plugin, final String dbLocation) {
        super(plugin);
        this.dbLocation = dbLocation;
    }

    @Override
    public Connection openConnection() {
        if (!plugin.getPlugin().getDataFolder().exists()) {
            plugin.getPlugin().getDataFolder().mkdirs();
        }
        final File file = new File(plugin.getPlugin().getDataFolder(), dbLocation);
        if (!(file.exists())) {
            try {
                file.createNewFile();
            } catch (final IOException e) {
                LogUtil.error("Unable to create database!");
            }
        }
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager
                    .getConnection("jdbc:sqlite:" + plugin.getPlugin().getDataFolder().toPath() + "/" + dbLocation);
        } catch (ClassNotFoundException | SQLException e) {
            LogUtil.error("There was an exception with SQL");
        }
        return connection;
    }
}
