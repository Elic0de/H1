package com.github.elic0de.h1.database;

import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.utils.LogUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Abstract Database class, serves as a base for any connection method (MySQL,
 * SQLite, etc.)
 */
@SuppressWarnings({"PMD.CommentRequired", "PMD.AvoidDuplicateLiterals"})
public abstract class Database {

    protected H1Plugin plugin;
    protected String prefix;
    protected Connection con;

    protected Database(final H1Plugin plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getConfigManager().getConfig().getStringElse("mysql.prefix", "h1");
    }

    public Connection getConnection() {
        if (con == null) {
            con = openConnection();
        }
        return con;
    }

    protected abstract Connection openConnection();

    public void closeConnection() {
        try {
            con.close();
        } catch (final SQLException e) {
            LogUtil.error("There was an exception with SQL");
        }
        con = null;
    }

    public void createTables(final boolean isMySQLUsed) {
        final String autoIncrement;
        if (isMySQLUsed) {
            autoIncrement = "AUTO_INCREMENT";
        } else {
            autoIncrement = "AUTOINCREMENT";
        }
        try {
            getConnection().createStatement()
                    .executeUpdate("CREATE TABLE IF NOT EXISTS " + prefix + "skills (id INTEGER PRIMARY KEY "
                            + autoIncrement + ", playerID VARCHAR(256) NOT NULL, skill TEXT NOT NULL);");
            getConnection().createStatement()
                    .executeUpdate("CREATE TABLE IF NOT EXISTS " + prefix + "player (id INTEGER PRIMARY KEY "
                            + autoIncrement + ", playerID VARCHAR(256) NOT NULL, level INT NOT NULL, point INT NOT NULL, blocks INT NOT NULL, max_mana INT NOT NULL,"
                            + "skill VARCHAR(512));");
        } catch (final SQLException e) {
            LogUtil.error("There was an exception with SQL");
        }
    }
}
