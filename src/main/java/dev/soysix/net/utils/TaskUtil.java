package dev.soysix.net.utils;

import dev.soysix.net.sCrate;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtil {

    public static void runTaskLater(Runnable runnable, long delay) {
        sCrate.getInst().getServer().getScheduler().runTaskLater(sCrate.getInst(), runnable, delay);
    }

    public static void runTask(Runnable runnable) {
        sCrate.getInst().getServer().getScheduler().runTask(sCrate.getInst(), runnable);
    }

}