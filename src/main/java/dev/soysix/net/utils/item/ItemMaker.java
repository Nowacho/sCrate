package dev.soysix.net.utils.item;

import dev.soysix.net.utils.CC;
import dev.soysix.net.utils.Callback;
import dev.soysix.net.utils.TaskUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ItemMaker {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    private Callback<Player> clicked;

    private Map<Action, Callback<Player>> interactables = new HashMap<>();
    private Map<Enchantment, Integer> enchantments = new HashMap<>();

    public ItemMaker(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemMaker(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemMaker(Material material, int page) {

    }

    public ItemMaker amount(int amount) {
        this.setAmount(amount);
        return this;
    }

    public ItemMaker setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemMaker setDisplayname(String name) {
        itemMeta.setDisplayName(CC.translate(name));
        return this;
    }

    public ItemMaker setDurability(int durability) {
        itemStack.setDurability((short) durability);
        return this;
    }

    public ItemMaker addLore(String lore) {
        Object object = itemMeta.getLore();
        if (object == null) object = new ArrayList<>();

        ((List) object).add(CC.translate(lore));
        itemMeta.setLore((List<String>) object);
        return this;
    }

    public ItemMaker addLore(List<String> lore) {
        itemMeta.setLore(CC.translate(lore));
        return this;
    }

    public ItemMaker addLore(String... lore) {
        List<String> strings = new ArrayList<>();
        for (String string : lore) {
            strings.add(CC.translate(string));
        }
        itemMeta.setLore(strings);
        return this;
    }

    public ItemMaker setEnchant(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
        return this;
    }

    public ItemMaker setPlayerInteract(Callback<Player> callback) {
        for (Action action : Action.values()) {
            action(action, callback);
        }

        return this;
    }

    public ItemMaker setInteractRight(Callback<Player> callback) {
        for (Action action : Action.values()) {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                action(action, callback);
            }
        }

        return this;
    }

    public ItemMaker setInteractLeft(Callback<Player> callback) {
        for (Action action : Action.values()) {
            if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                action(action, callback);
            }
        }

        return this;
    }

    private void action(Action action, Callback<Player> callback) {
        interactables.put(action, callback);
    }

    public ItemMaker action(Predicate<Action> pred, Callback<Player> callback) {
        for (Action action : Action.values()) {
            if (pred.test(action)) {
                action(action, callback);
            }
        }
        return this;
    }

    public ItemMaker setUnbreakable(boolean unbreakable) {
        itemMeta.spigot().setUnbreakable(unbreakable);
        return this;
    }

    public ItemMaker setColor(Color color) {
        if (itemStack.getType() != null && itemStack.getType().name().contains("LEATHER")) {
            LeatherArmorMeta armorMeta = (LeatherArmorMeta) itemMeta;
            armorMeta.setColor(color);
        }
        return this;
    }

    public ItemStack create() {
        if (itemMeta != null) {
            itemStack.setItemMeta(itemMeta);
        }

        enchantments.forEach((enchantment, integer) -> itemStack.addUnsafeEnchantment(enchantment, integer));

        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbt = nmsStack.getTag();

        if (nbt == null) {
            nbt = new NBTTagCompound();
        }

        for (Map.Entry<Action, Callback<Player>> entry : interactables.entrySet()) {
            nbt.set("interact_" + entry.getKey().name(), new ExecutableNBTTag(entry.getValue()));
        }

        if (clicked != null) {
            nbt.set("clicked", new ExecutableNBTTag(clicked));
        }

        nmsStack.setTag(nbt);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public static class ItemMakerListener implements Listener {

        @EventHandler
        public void onPlayerDropItem(PlayerDropItemEvent event) {
            ItemStack item = event.getItemDrop().getItemStack();

            if (item != null) {
                net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

                if (nmsStack.hasTag()) {

                    for (Action action : Action.values()) {
                        String key = "interact_" + action.name();
                        if (nmsStack.getTag().hasKey(key)) {
                            NBTTagString e = (NBTTagString) nmsStack.getTag().get(key);

                            if (e instanceof ExecutableNBTTag) {
                                event.setCancelled(true);
                                return;
                            }
                        }
                    }
                }
            }
        }

        @EventHandler
        public void onPlayerInventoryClick(InventoryClickEvent event) {
            ItemStack item = event.getCurrentItem();

            if (item != null) {
                net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

                if (nmsStack != null && nmsStack.hasTag()) {

                    String key = "clicked";
                    if (nmsStack.getTag().hasKey(key)) {
                        NBTTagString e = (NBTTagString) nmsStack.getTag().get(key);

                        if (e instanceof ExecutableNBTTag) {
                            event.setCancelled(true);
                            TaskUtil.runTask(() -> ((ExecutableNBTTag) e).execute((Player) event.getWhoClicked()));
                        }
                    }
                }
            }
        }

        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent event) {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();
            Action action = event.getAction();

            if (item != null) {
                net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

                if (nmsStack.hasTag()) {

                    String key = "interact_" + action.name();
                    if (nmsStack.getTag().hasKey(key)) {
                        NBTTagString e = (NBTTagString) nmsStack.getTag().get(key);

                        if (e instanceof ExecutableNBTTag) {

                            ((ExecutableNBTTag) e).execute(player);
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}