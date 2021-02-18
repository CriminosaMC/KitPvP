package dev.criminosa.kitpvp.listeners;

import dev.criminosa.kitpvp.user.UserManager;
import dev.criminosa.kitpvp.db.Database;
import dev.criminosa.kitpvp.main.Main;
import dev.criminosa.kitpvp.user.User;
import dev.criminosa.kitpvp.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PlayerListener implements Listener {

    private final Main plugin;

    public PlayerListener(Main plugin){
        this.plugin = plugin;
    }

    // TODO: Rewrite this function
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        User user = UserManager.addUser(event.getPlayer());
        // If user is not present in database, add them to the database
        if(!Database.isUserInDatabase(user)) Database.addUserToDatabase(user);
        event.setJoinMessage(null);
        user.sendMessage("&6&lKitPvP&8 »&7 Welcome back, &6" + user.getPlayer().getName() + "&7.");
        // Create the scoreboard and set it up
        Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
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
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            kills.setSuffix("§6" + user.getKills());
            deaths.setSuffix("§6" + user.getDeaths());
            credits.setSuffix("§6" + user.getCredits());
        }, 0L, 2L);
        user.getPlayer().setScoreboard(scoreboard);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UserManager.removeUser(event.getPlayer());
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onPlayerLeave(PlayerKickEvent event) {
        UserManager.removeUser(event.getPlayer());
        event.setLeaveMessage(null);
    }

}
