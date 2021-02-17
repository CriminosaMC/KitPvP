package dev.criminosa.kitpvp.db;

import dev.criminosa.kitpvp.user.User;
import dev.criminosa.kitpvp.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.*;

public class Database {

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(Main.getConnectionURL());
        } catch(ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        return connection;
    }

    public static void init() {
        Connection connection = getConnection();
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Players(PLAYERID int NOT NULL IDENTITY(1, 1), UUID varchar, Kills int, Deaths int, Credits int);");

            statement.execute();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isUserInDatabase(User user) {
        Connection connection = getConnection();
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("SELECT * FROM Players WHERE UUID = ?");
            statement.setString(1, user.getUUID());

            ResultSet rows = statement.executeQuery();
            return rows.next();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void addUserToDatabase(User user) {
        Connection connection = getConnection();
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("INSERT INTO Players(UUID, Kills, Deaths, Credits) VALUES(?, ?, ?, ?);");
            statement.setString(1, user.getUUID());
            statement.setInt(2, 0);
            statement.setInt(3, 0);
            statement.setInt(4, 0);

            statement.execute();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isUserInDatabase(String name) {
        Connection connection = getConnection();
        PreparedStatement statement;

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        if(offlinePlayer == null) return false;
        try {
            statement = connection.prepareStatement("SELECT * FROM Players WHERE UUID = ?");
            statement.setString(1, offlinePlayer.getUniqueId().toString());

            ResultSet rows = statement.executeQuery();
            return rows.next();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static ResultSet getDataForUser(String username) {
        if(!isUserInDatabase(username)) return null;
        Connection connection = getConnection();
        PreparedStatement statement;

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
        if(offlinePlayer == null) return null;
        try {
            statement = connection.prepareStatement("SELECT * FROM Players WHERE UUID = ?");
            statement.setString(1, offlinePlayer.getUniqueId().toString());

            ResultSet rows = statement.executeQuery();
            rows.absolute(1);
            return rows;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
