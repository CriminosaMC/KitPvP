package dev.criminosa.kitpvp.user;

import dev.criminosa.kitpvp.main.Main;
import dev.criminosa.kitpvp.utils.StringUtils;
import dev.criminosa.kitpvp.db.Database;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    private final Player player;
    private int kills;
    private int deaths;
    private int credits;

    public User(Player player) {
        this.player = player;
        updateData();
    }

    public void sendMessage(String message) {
        player.sendMessage(StringUtils.cc(message));
    }

    public ResultSet getData() {
        Connection connection = Database.getConnection();
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("SELECT * FROM Players WHERE UUID = ?");
            statement.setString(1, getUUID());

            ResultSet query = statement.executeQuery();
            query.absolute(1);
            return query;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getUUID() {
        return player.getUniqueId().toString();
    }

    public Player getPlayer() {
        return player;
    }

    public void updateData() {
        ResultSet data = getData();
        if(data != null) {
            try {
                kills = data.getInt("Kills");
                deaths = data.getInt("Deaths");
                credits = data.getInt("Credits");
            } catch(SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void onJoin() {
        sendMessage("&6&lKitPvP&8 »&7 Welcome back, &6" + player.getName() + "&7.");
        // Create the scoreboard and set it up
        Scoreboard scoreboard = Main.getInstance().getServer().getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("KitPvP", "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(StringUtils.cc("&6&lKitPvP &c[Beta]"));
        // Register all the teams
        Team divider1, divider2, kills, deaths, credits;

        divider1 = scoreboard.registerNewTeam("divider1");
        divider2 = scoreboard.registerNewTeam("divider2");
        kills = scoreboard.registerNewTeam("Kills");
        deaths = scoreboard.registerNewTeam("Deaths");
        credits = scoreboard.registerNewTeam("Credits");
        // Add all the entries & suffixes
        divider1.addEntry("§7§m-----------");
        divider2.addEntry("§7§m------------");
        kills.addEntry(StringUtils.cc("&6&lKills&7: "));
        deaths.addEntry(StringUtils.cc("&6&lDeaths&7: "));
        credits.addEntry(StringUtils.cc("&6&lCredits&7: "));

        divider1.setSuffix("-----------");
        divider2.setSuffix("----------");
        kills.setSuffix("");
        deaths.setSuffix("");
        credits.setSuffix("");

        kills.setPrefix("");
        deaths.setPrefix("");
        credits.setPrefix("");
        // Set the scores
        objective.getScore("§7§m-----------").setScore(-1);
        objective.getScore("§6§lKills§7: ").setScore(-2);
        objective.getScore("§6§lDeaths§7: ").setScore(-3);
        objective.getScore("§6§lCredits§7: ").setScore(-4);
        objective.getScore("§7§m------------").setScore(-5);

        // Schedule a task
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            kills.setSuffix("§6" + getKills());
            deaths.setSuffix("§6" + getDeaths());
            credits.setSuffix("§6" + getCredits());
        }, 0L, 2L);
        player.setScoreboard(scoreboard);
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getCredits() {
        return credits;
    }

}
