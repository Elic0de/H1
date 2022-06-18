package com.github.elic0de.h1.database;

import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.player.H1Player;
import com.github.elic0de.h1.utils.LogUtil;
import com.github.elic0de.h1.utils.PlayerConverter;
import com.github.elic0de.h1.utils.enums.SkillType;
import github.scarsz.configuralize.DynamicConfig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents an object storing all player-related data, which can load and save it.
 */
@SuppressWarnings({"PMD.TooManyMethods", "PMD.CommentRequired"})
public class PlayerData {

    @SuppressWarnings("PMD.DoNotUseThreads")
    private final Saver saver = H1Plugin.INSTANCE.getSaver();

    private final List<String> skills = new CopyOnWriteArrayList<>();
    private final String playerID;
    private final H1Player h1Player;

    /**
     * Creates new PlayerData for the player represented by playerID.
     *
     * @param playerID - ID of the player
     */
    public PlayerData(final String playerID, H1Player h1Player) {
        this.playerID = playerID;
        this.h1Player = h1Player;
        // load data from the database
        loadAllPlayerData();
    }

    /**
     * Loads all data for the player and puts it in appropriate lists.
     */
    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity", "PMD.CognitiveComplexity"})
    public final void loadAllPlayerData() {
        try {
            final Connector con = new Connector();

            try (ResultSet res1 = con.querySQL(Connector.QueryType.SELECT_SKILLS, playerID);
                 ResultSet res2 = con.querySQL(Connector.QueryType.SELECT_PLAYER, playerID)) {

                // put them into the list
                while (res1.next()) {
                    skills.add(res1.getString("skill"));
                }

                // put it there
                if (res2.next()) {
                    int level = res2.getInt("level");
                    int point = res2.getInt("point");
                    int blocks = res2.getInt("blocks");
                    int maxMana = res2.getInt("max_mana");

                    h1Player.setLevel(level);
                    h1Player.setPoint(point);
                    h1Player.setBreakBlocks(blocks);
                    h1Player.setMaxMana(maxMana);

                    String skill = res2.getString("skill");
                    if (skill == null || skill.equalsIgnoreCase("null")) {
                        h1Player.setSkill(SkillType.NONE);
                    } else {
                        h1Player.setSkill(SkillType.valueOf(skill));
                    }
                } else {
                    DynamicConfig config = H1Plugin.INSTANCE.getConfigManager().getConfig();
                    int level = config.getIntElse("default.level", 0);
                    int point = config.getIntElse("default.point", 0);
                    int blocks = config.getIntElse("default.blocks", 0);
                    int maxMana = config.getIntElse("default.maxMana", 200);
                    saver.add(new Saver.Record(Connector.UpdateType.ADD_PLAYER, playerID, String.valueOf(level), String.valueOf(point), String.valueOf(maxMana), String.valueOf(blocks)));
                }

                // log data to debugger
                LogUtil.info("loaded for player " + PlayerConverter.getName(playerID));
            }
        } catch (final SQLException e) {
            LogUtil.error("There was an exception with SQL");
        }
    }

    public List<String> getSkills() {
        return skills;
    }

    public boolean hasSkill(final String skill) {
        return skills.contains(skill);
    }

    public void addSkill(final String skill) {
        synchronized (skill) {
            if (!skills.contains(skill)) {
                skills.add(skill);
                saver.add(new Saver.Record(Connector.UpdateType.ADD_SKILLS, playerID, skill));
            }
        }
    }

    public void saveData() {
        final String level = String.valueOf(h1Player.getLevel());
        final String point = String.valueOf(h1Player.getPoint());
        final String blocks = String.valueOf(h1Player.getBreakBlocks());
        final String skill = h1Player.getType().name();
        final String maxMana = String.valueOf(h1Player.getMaxMana());
        saver.add(new Saver.Record(Connector.UpdateType.UPDATE_PLAYER, level, point, blocks, maxMana, skill, playerID));
    }
}
