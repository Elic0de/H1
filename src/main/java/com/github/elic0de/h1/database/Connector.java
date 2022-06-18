package com.github.elic0de.h1.database;

import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.utils.LogUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Connects to the database and queries it
 */
@SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.CommentRequired", "PMD.AvoidDuplicateLiterals"})
public class Connector {

    private final String prefix;
    private final Database database;
    private Connection connection;

    /**
     * Opens a new connection to the database
     */
    public Connector() {
        final H1Plugin plugin = H1Plugin.INSTANCE;
        prefix = plugin.getConfigManager().getConfig().getStringElse("mysql.prefix", "h1");
        database = plugin.getDatabase();
        connection = database.getConnection();
        refresh();
    }

    /**
     * This method should be used before any other database operations.
     */
    public final void refresh() {
        try {
            connection.prepareStatement("SELECT 1").executeQuery().close();
        } catch (final SQLException e) {
            LogUtil.warn("Reconnecting to the database");
            database.closeConnection();
            connection = database.getConnection();
        }
    }

    /**
     * Queries the database with the given type and arguments
     *
     * @param type type of the query
     * @param args arguments
     * @return ResultSet with the requested data
     */
    @SuppressWarnings({"PMD.NcssCount", "PMD.CloseResource"})
    public ResultSet querySQL(final QueryType type, final String... args) {
        final String stringStatement;
        switch (type) {
            case SELECT_SKILLS:
                stringStatement = "SELECT skill FROM " + prefix + "skills WHERE playerID = ?;";
                break;
            case SELECT_PLAYER:
                stringStatement = "SELECT * FROM " + prefix + "player WHERE playerID = ?;";
                break;
            case LOAD_ALL_PLAYER:
                stringStatement = "SELECT * FROM " + prefix + "player";
                break;
            default:
                stringStatement = "SELECT 1";
                break;
        }

        try {
            final PreparedStatement statement = connection.prepareStatement(stringStatement);
            for (int i = 0; i < args.length; i++) {
                statement.setString(i + 1, args[i]);
            }
            return statement.executeQuery();
        } catch (final SQLException e) {
            LogUtil.warn("There was a exception with SQL");
            return null;
        }
    }

    /**
     * Updates the database with the given type and arguments
     *
     * @param type type of the update
     * @param args arguments
     */
    @SuppressWarnings({"PMD.ExcessiveMethodLength", "PMD.NcssCount", "PMD.NPathComplexity"})
    public void updateSQL(final UpdateType type, final String... args) {
        final String stringStatement;
        switch (type) {
            case ADD_SKILLS:
                stringStatement = "INSERT INTO " + prefix + "skills (playerID, skill) VALUES (?, ?);";
                break;
            case ADD_PLAYER:
                stringStatement = "INSERT INTO " + prefix + "player (playerID, level, point, blocks, max_mana) VALUES (?, ?, ?, ?, ?);";
                break;
            case DELETE_PLAYER:
                stringStatement = "DELETE FROM " + prefix + "player WHERE playerID = ?;";
                break;
            case DROP_PLAYER:
                stringStatement = "DROP TABLE " + prefix + "player";
                break;
            case INSERT_PLAYER:
                stringStatement = "INSERT INTO " + prefix + "player VALUES (?,?,?,?);";
                break;
            case UPDATE_PLAYER:
                stringStatement = "UPDATE " + prefix + "player SET level = ?, point = ?, blocks = ?, max_mana = ?, skill = ? WHERE playerID = ?";
                break;
            default:
                stringStatement = "SELECT 1";
                break;
        }

        try (PreparedStatement statement = connection.prepareStatement(stringStatement)) {
            for (int i = 0; i < args.length; i++) {
                statement.setString(i + 1, args[i]);
            }
            statement.executeUpdate();
        } catch (final SQLException e) {
            LogUtil.error("There was an exception with SQL");
        }
    }

    /**
     * Type of the query
     */
    public enum QueryType {

        SELECT_SKILLS,

        SELECT_PLAYER,

        LOAD_ALL_PLAYER,

    }

    /**
     * Type of the update
     */
    public enum UpdateType {

        ADD_SKILLS,

        ADD_PLAYER,

        DELETE_PLAYER,

        DROP_PLAYER,

        INSERT_PLAYER,

        UPDATE_PLAYER,

    }

}
