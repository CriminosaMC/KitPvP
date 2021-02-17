package dev.criminosa.kitpvp.user;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class UserManager {

    private static final HashMap<Player, User> users = new HashMap<>();

    public static User addUser(Player player) {
        if(users.containsKey(player)) return users.get(player);
        users.put(player, new User(player));
        return users.get(player);
    }

    public static User getUser(Player player) {
        return users.get(player);
    }

    public static void removeUser(Player player) {
        users.remove(player);
    }

    public static HashMap<Player, User> getUsers() {
        return users;
    }

}
