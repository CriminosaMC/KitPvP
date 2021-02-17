package dev.criminosa.kitpvp.listeners;

import dev.criminosa.kitpvp.user.UserManager;
import dev.criminosa.kitpvp.db.Database;
import dev.criminosa.kitpvp.main.Main;
import dev.criminosa.kitpvp.user.User;
import dev.criminosa.kitpvp.utils.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerListener implements Listener {

    private Main plugin;

    public PlayerListener(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        User user = UserManager.addUser(event.getPlayer());
        if(!Database.isUserInDatabase(user)) Database.addUserToDatabase(user);
        event.setJoinMessage(null);
        user.sendMessage("&6&lKitPvP&8 »&7 Welcome back, &6" + user.getPlayer().getName() + "&7.");
        Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("KitPvP", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(StringUtils.cc("&6&lKitPvP &c[Beta]"));
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
