package dev.soysix.net.utils.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MakerListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() == null) {
            return;
        }
        if (!(event.getInventory().getHolder() instanceof InventoryMaker.InventoryMakerHolder)) {
            return;
        }
        if (event.getCurrentItem() == null) {
            return;
        }

        InventoryMaker.InventoryMakerHolder inventoryUIHolder = (InventoryMaker.InventoryMakerHolder) event.getInventory().getHolder();

        event.setCancelled(true);

        if (event.getClickedInventory() == null || !event.getInventory().equals(event.getClickedInventory())) {
            return;
        }
        InventoryMaker ui = inventoryUIHolder.getInventoryMaker();
        InventoryMaker.ClickableItem item = ui.getCurrentUI().getItem(event.getSlot());

        if (item != null) {
            item.onClick(event);
        }
    }
}
