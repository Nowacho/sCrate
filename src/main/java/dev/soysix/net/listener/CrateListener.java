package dev.soysix.net.listener;

import dev.soysix.net.crate.Crate;
import dev.soysix.net.crate.CrateReward;
import dev.soysix.net.events.OpenCrateEvent;
import dev.soysix.net.utils.BukkitUtils;
import dev.soysix.net.utils.inventory.InventoryMaker;
import dev.soysix.net.utils.item.ItemMaker;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CrateListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().name().contains("BLOCK")) {
            if (event.getClickedBlock().getType() != Material.ENDER_CHEST) {
                return;
            }
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasItem()) {

                ItemStack itemInHand = event.getItem();
                Crate crate = Crate.getByItem(itemInHand);

                if (!Crate.isCrate(itemInHand) || crate == null) {
                    return;
                }
                event.setCancelled(true);
                event.setUseItemInHand(Event.Result.DENY);
                event.setUseInteractedBlock(Event.Result.DENY);

                if (event.getClickedBlock().getType() == Material.ENDER_CHEST) {
                    int randomNumber = BukkitUtils.getRandomNumber(100);
                    CrateReward crateReward = crate.getPlayerCrates().stream()
                            .filter(crateReward1 -> crateReward1.getChance() >= randomNumber
                            ).findAny().orElse(crate.getPlayerCrates().get(BukkitUtils.getRandomNumber(crate.getPlayerCrates().size())));

                    if (!new OpenCrateEvent(player, crate, crateReward).call()) {
                        return;
                    }

                    crateReward.getCallback().callback(player);
                    if (itemInHand.getAmount() > 1) {
                        itemInHand.setAmount(itemInHand.getAmount() - 1);
                    } else {
                        player.setItemInHand(null);
                    }
                    player.updateInventory();
                }
            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                InventoryMaker inventoryMaker = new InventoryMaker("&7Select a crate", 1);

                Crate.getCrates().forEach(crate -> inventoryMaker.addItem(new InventoryMaker.ClickableItem() {
                    @Override
                    public void onClick(InventoryClickEvent inventoryClickEvent) {
                        InventoryMaker maker = new InventoryMaker(crate.getPrefix() + crate.getName() + " Preview", 3);

                        maker.setItem(22, new InventoryMaker.ClickableItem() {
                            @Override
                            public void onClick(InventoryClickEvent inventoryClickEvent) {
                                player.openInventory(inventoryMaker.getCurrentPage());
                            }

                            @Override
                            public ItemStack getItemStack() {
                                return new ItemMaker(Material.BED).setDisplayname("&cClose").create();
                            }
                        });

                        crate.getInventoryData().forEach((itemStack, integer) -> maker.setItem(integer, new InventoryMaker.ClickableItem() {
                            @Override
                            public void onClick(InventoryClickEvent inventoryClickEvent) {

                            }

                            @Override
                            public ItemStack getItemStack() {
                                return itemStack;
                            }
                        }));

                        player.openInventory(maker.getCurrentPage());
                    }

                    @Override
                    public ItemStack getItemStack() {
                        return new ItemMaker(Material.CHEST).setDisplayname(crate.getPrefix() + crate.getName()).addLore("&eRight-click to open crate preview").create();
                    }
                }));

                player.openInventory(inventoryMaker.getCurrentPage());
            }
        }
    }

}