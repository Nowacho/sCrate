package dev.soysix.net.utils;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public final class CC {

    public static String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> translate(List<String> list) {
        return (List<String>)list.stream().map(CC::translate).collect(Collectors.toList());
    }
}
