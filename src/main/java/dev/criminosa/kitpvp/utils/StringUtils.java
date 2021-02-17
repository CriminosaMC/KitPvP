package dev.criminosa.kitpvp.utils;

import org.bukkit.ChatColor;

public class StringUtils {

    public static String cc(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
