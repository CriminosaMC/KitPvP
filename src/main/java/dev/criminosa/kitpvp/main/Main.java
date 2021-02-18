package dev.criminosa.kitpvp.main;

import dev.criminosa.kitpvp.commands.CmdStats;
import dev.criminosa.kitpvp.db.Database;
import dev.criminosa.kitpvp.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static String connectionURL;
    private static Main instance;

    public void onEnable() {
        instance = this;
        // set up connection URI
        connectionURL = "jdbc:h2:" + getDataFolder().getAbsolutePath() + "/data/players";
        getLogger().info("Connecting to database...");
        Database.init();
        // if connection is null it means the init() function failed
        if(Database.getConnection() == null) {
            getLogger().severe("Database connection was null after initialisation, shutting down plugin");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Connected to database.");
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getCommand("stats").setExecutor(new CmdStats());
    }

    public static String getConnectionURL() {
        return connectionURL;
    }

    public static Main getInstance() {
        return instance;
    }

}
