package dev.soysix.net.utils.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    public static boolean isSimilar(ItemStack stack, ItemStack itemStack) {
        return stack.getType() == itemStack.getType() && (stack.hasItemMeta() && (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(stack.getItemMeta().getDisplayName())));
    }

    public static void addOrDrop(Player player, ItemStack itemStack) {
        for (ItemStack stack : player.getInventory().addItem(itemStack).values()) {
            player.getWorld().dropItem(player.getLocation(), stack);
        }
    }

    public static void decrement(Player player) {
        ItemStack stack = player.getItemInHand();
        if (stack.getAmount() > 1) {
            stack.setAmount(stack.getAmount() - 1);
        } else {
            player.setItemInHand(null);
        }
        player.updateInventory();
    }
}