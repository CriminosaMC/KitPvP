package dev.criminosa.kitpvp.listeners;

import dev.criminosa.kitpvp.user.UserManager;
import dev.criminosa.kitpvp.db.Database;
import dev.criminosa.kitpvp.main.Main;
import dev.criminosa.kitpvp.user.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final Main plugin;

    public PlayerListener(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        User user = UserManager.addUser(event.getPlayer());
        // If user is not present in database, add them to the database
        if(!Database.isUserInDatabase(user)) Database.addUserToDatabase(user);
        event.setJoinMessage(null);
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
