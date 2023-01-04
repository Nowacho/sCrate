package dev.soysix.net.crate;

import dev.soysix.net.sCrate;
import dev.soysix.net.utils.BukkitUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public abstract class Crate implements Listener {

    @Getter
    protected static List<Crate> crates = new ArrayList<>();

    @Getter
    private String prefix, name;

    public abstract ItemStack getIcon();

    public abstract List<CrateReward> getPlayerCrates();

    public abstract Map<ItemStack, Integer> getInventoryData();

    public static Crate getByName(String name) {
        return crates.stream().filter(crate -> crate.getName().replace(" ", "").equalsIgnoreCase(name.replace(" ", ""))).findFirst().orElse(null);
    }

    public static void register(Crate crate) {
        if (getCrates().contains(crate)) {
            BukkitUtils.sendToConsole("&d" + crate.getName() + " &eis already loaded.");
            return;
        }
        getCrates().add(crate);
        Bukkit.getPluginManager().registerEvents(crate, sCrate.getInst());
        BukkitUtils.sendToConsole("&d" + crate.getName() + " &ewas successfully loaded.");
    }

    public static Crate getByItem(ItemStack itemStack) {
        return crates.stream().filter(crate -> crate.getIcon().isSimilar(itemStack)).findFirst().orElse(null);
    }

    public static boolean isCrate(ItemStack stack) {
        for (Crate crate : crates) {
            if (crate.getIcon().isSimilar(stack)) {
                return true;
            }
        }
        return false;
    }

}
