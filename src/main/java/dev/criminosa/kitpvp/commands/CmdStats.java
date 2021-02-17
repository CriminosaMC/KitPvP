package dev.criminosa.kitpvp.commands;

import dev.criminosa.kitpvp.db.Database;
import dev.criminosa.kitpvp.user.User;
import dev.criminosa.kitpvp.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CmdStats implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            User user = UserManager.getUser((Player) sender);
            if(args.length <= 0) {
                // Get own user data
                ResultSet data = user.getData();
                int kills, deaths, credits;
                try {
                    kills = data.getInt("Kills");
                    deaths = data.getInt("Deaths");
                    credits = data.getInt("Credits");
                } catch(SQLException ex) {
                    ex.printStackTrace();
                    user.sendMessage("&6&lKitPvP&8 »&c An internal error occurred while fetching your user data. Please report this to a developer.");
                    return true;
                }
                user.sendMessage("&6&lKitPvP&8 »&7 Your stats:");
                user.sendMessage("&6&lKitPvP&8 »&7 Kills: &6" + kills);
                user.sendMessage("&6&lKitPvP&8 »&7 Deaths: &6" + deaths);
                user.sendMessage("&6&lKitPvP&8 »&7 Credits: &6" + credits);
                return true;
            }
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if(offlinePlayer == null) {
                user.sendMessage("&6&lKitPvP&8 »&7 That user does not exist.");
                return true;
            }
            ResultSet data = Database.getDataForUser(args[0]);
            if(data == null) {
                user.sendMessage("&6&lKitPvP&8 »&7 That user has never joined the server before.");
                return true;
            }
            int kills, deaths, credits;
            try {
                kills = data.getInt("Kills");
                deaths = data.getInt("Deaths");
                credits = data.getInt("Credits");
            } catch(SQLException ex) {
                ex.printStackTrace();
                user.sendMessage("&6&lKitPvP&8 »&c An internal error occurred while fetching this user's data. Please report this to a developer.");
                return true;
            }
            user.sendMessage("&6&lKitPvP&8 »&6 " + offlinePlayer.getName() + "&7's stats:");
            user.sendMessage("&6&lKitPvP&8 »&7 Kills: &6" + kills);
            user.sendMessage("&6&lKitPvP&8 »&7 Deaths: &6" + deaths);
            user.sendMessage("&6&lKitPvP&8 »&7 Credits: &6" + credits);
            return true;
        }
        return false;
    }

}
