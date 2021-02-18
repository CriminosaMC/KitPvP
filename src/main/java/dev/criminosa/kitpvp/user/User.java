package dev.criminosa.kitpvp.user;

import dev.criminosa.kitpvp.utils.StringUtils;
import dev.criminosa.kitpvp.db.Database;
import org.bukkit.entity.Player;

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
