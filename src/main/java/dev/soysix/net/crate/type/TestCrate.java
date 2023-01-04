package dev.soysix.net.crate.type;

import dev.soysix.net.crate.Crate;
import dev.soysix.net.crate.CrateReward;
import dev.soysix.net.utils.BukkitUtils;
import dev.soysix.net.utils.cooldown.Cooldown;
import dev.soysix.net.utils.CC;
import dev.soysix.net.utils.inventory.InventoryUtils;
import dev.soysix.net.utils.item.ItemMaker;
import dev.soysix.net.utils.TaskUtil;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestCrate extends Crate {

    public TestCrate() {
        super("&b&l", "Test Crate");
    }

    @Override
    public ItemStack getIcon() {
        return new ItemMaker(Material.BOOK).setDisplayname(getPrefix() + getName()).setEnchant(Enchantment.DURABILITY, 10).create();
    }

    @Override
    public List<CrateReward> getPlayerCrates() {
        List<CrateReward> crates = new ArrayList<>();
        ItemStack itemStack = new ItemMaker(Material.GOLD_AXE).setDisplayname("&e&lThor's Axe").setEnchant(Enchantment.DAMAGE_ALL, 5).create();
        ItemStack finalItemStack = itemStack;
        crates.add(new CrateReward(itemStack, player -> InventoryUtils.addOrDrop(player, finalItemStack), 20));
        itemStack = new ItemMaker(Material.WEB).setDisplayname("&f&lSpiderman Cobwebs").setAmount(7).create();
        ItemStack finalItemStack2 = itemStack;
        crates.add(new CrateReward(new ItemMaker(Material.ANVIL).setDisplayname("&9&lRepair Token").addLore("&7You can use every 2 minutes.").create(), player -> InventoryUtils.addOrDrop(player, new ItemMaker(Material.DIAMOND_BLOCK).setDisplayname("&9&lRepair Token").create()), 20));
        crates.add(new CrateReward(new ItemMaker(Material.SUGAR).setDisplayname("&b&lSpeed III").addLore("&7Speed 3 for 10 seconds").create(), player -> InventoryUtils.addOrDrop(player, new ItemMaker(Material.SUGAR).setDisplayname("&b&lSpeed III").addLore("&7Click to receive speed 3 for 10 seconds").create()), 23));
        crates.add(new CrateReward(new ItemMaker(Material.BLAZE_POWDER).setDisplayname("&c&lStrength I").addLore("&7Strength I for 10 seconds").create(), player -> InventoryUtils.addOrDrop(player, new ItemMaker(Material.BLAZE_POWDER).setDisplayname("&c&lStrength I").addLore("&7Click to receive strength 1 for 10 seconds").create()), 27));
        crates.add(new CrateReward(new ItemMaker(Material.ENDER_PEARL).setDisplayname("&5&lEnderpearl &7(x2)").setAmount(2).create(), player -> InventoryUtils.addOrDrop(player, new ItemMaker(Material.ENDER_PEARL).setAmount(2).create()), 29));
        return crates;
    }

    @Override
    public Map<ItemStack, Integer> getInventoryData() {
        Map<ItemStack, Integer> map = new HashMap<>();
        int start = 10;
        for (CrateReward playerCrate : getPlayerCrates()) {
            map.put(playerCrate.getItemStack(), start);
            start += 2;
        }
        return map;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasItem()) {
            Player player = event.getPlayer();
            if (!event.getAction().name().startsWith("RIGHT")) {
                return;
            }
            ItemStack stack = player.getItemInHand();
            List<CrateReward> rewards = getPlayerCrates();
            if (InventoryUtils.isSimilar(stack, rewards.get(0).getItemStack())) {
                Cooldown cooldown = BukkitUtils.getCooldown("REPAIR_TOKEN");
                if (cooldown.isOnCooldown(player)) {
                    player.sendMessage(CC.translate("&cYou're on cooldown for another " + DurationFormatUtils.formatDurationWords(cooldown.getDuration(player), true, true) + '.'));
                    return;
                }
                cooldown.setCooldown(player);
                InventoryUtils.decrement(player);

                for (ItemStack itemStack : player.getInventory().getContents()) {
                    if (itemStack == null || itemStack.getType() == Material.AIR || !isArmor(itemStack)) {
                        continue;
                    }
                    itemStack.setDurability((short) 0);
                }

                for (ItemStack itemStack : player.getInventory().getArmorContents()) {
                    if (itemStack == null || itemStack.getType() == Material.AIR || !isArmor(itemStack)) {
                        continue;
                    }
                    itemStack.setDurability((short) 0);
                }
                player.sendMessage(CC.translate("&aInventory successfully repaired."));
            } else if (InventoryUtils.isSimilar(stack, rewards.get(1).getItemStack())) {
                InventoryUtils.decrement(player);
                player.sendMessage(CC.translate("&aSpeed 3 activated for 10 seconds."));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200000, 2), true);
                TaskUtil.runTaskLater(() -> player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200000, 1), true), 10 * 20L);
            } else if (InventoryUtils.isSimilar(stack, rewards.get(2).getItemStack())) {
                InventoryUtils.decrement(player);
                player.sendMessage(CC.translate("&aStrength activated for 10 seconds."));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200000, 0), true);
                TaskUtil.runTaskLater(() -> player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE), 10 * 20L);
            }
        }
    }

    private boolean isSimilar(ItemStack stack, ItemStack itemStack) {
        return stack.getType() == itemStack.getType();
    }

    private boolean isArmor(ItemStack itemStack) {
        return itemStack.getType() == Material.DIAMOND_HELMET || itemStack.getType() == Material.DIAMOND_CHESTPLATE || itemStack.getType() == Material.DIAMOND_LEGGINGS || itemStack.getType() == Material.DIAMOND_BOOTS;
    }

}