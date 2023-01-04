package dev.soysix.net.utils;

import com.google.common.base.Preconditions;
import dev.soysix.net.utils.cooldown.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class BukkitUtils {

    public static String STAFF_PERMISSION = "";
    public static String PERMISSION = "";

    public static List<Player> getOnlineStaff() {
        List<Player> players = new ArrayList<>();
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (online.hasPermission(STAFF_PERMISSION)) {
                players.add(online);
            }
        }
        return players;
    }

    public static List<Player> getOnlineOperators() {
        List<Player> players = new ArrayList<>();
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (online.isOp()) {
                players.add(online);
            }
        }
        return players;
    }

    public static String getPlayerNotFoundMessage(String name) {
        return CC.translate("&cPlayer with name &4" + name + " &cnot found.");
    }

    public static Integer tryParseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static boolean isOnline(OfflinePlayer target) {
        return target != null && target.isOnline();
    }

    public static int getRandomNumber(int random) {
        return new Random().nextInt(random);
    }

    public static List<String> getCompletions(String[] args, List<String> input) {
        return getCompletions(args, input, 80);
    }

    private static List<String> getCompletions(String[] args, List<String> input, int limit) {
        Preconditions.checkNotNull((Object) args);
        Preconditions.checkArgument(args.length != 0);
        String argument = args[args.length - 1];
        return input.stream().filter(string -> string.regionMatches(true, 0, argument, 0, argument.length())).limit(limit).collect(Collectors.toList());
    }

    public static Cooldown getCooldown(String name) {
        return Cooldown.getCooldownMap().get(name);
    }

    public static void sendToConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate(message));
    }
}