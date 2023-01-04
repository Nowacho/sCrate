package dev.soysix.net.commands.chat.arguments;

import com.google.common.primitives.Ints;
import dev.soysix.net.crate.Crate;
import dev.soysix.net.events.ReceiveCrateEvent;
import dev.soysix.net.utils.BukkitUtils;
import dev.soysix.net.utils.CC;
import dev.soysix.net.utils.command.CommandArgument;
import dev.soysix.net.utils.item.ItemMaker;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CrateGiveArgument extends CommandArgument {

    public CrateGiveArgument() {
        super("give");
    }

    @Override
    public String getUsage(String s) {
        return '/' + s + ' ' + getName() + " <playerName> <crateName> <amount>";
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(CC.translate("&cUsage: " + getUsage(label)));
        } else {
            Player target = Bukkit.getPlayer(args[1]);
            if (!BukkitUtils.isOnline(target)) {
                sender.sendMessage(BukkitUtils.getPlayerNotFoundMessage(args[1]));
                return;
            }
            Crate crate = Crate.getByName(args[2]);
            if (crate == null) {
                sender.sendMessage(CC.translate("&cA crate with that name was not found."));
                return;
            }
            if (!new ReceiveCrateEvent(target, crate).call()) {
                return;
            }
            Integer amount = null;
            try {
                amount = Ints.tryParse(args[3]);
            } catch (Exception ignored) {
                sender.sendMessage(CC.translate("&cInvalid amount."));
                return;
            }
            if (amount < 1) {
                sender.sendMessage(CC.translate("&cAmount must be more than 0."));
                return;
            }
            for (ItemStack stack : target.getInventory().addItem(new ItemMaker(crate.getIcon()).setAmount(amount).create()).values()) {
                target.getWorld().dropItem(target.getLocation(), stack);
            }
            sender.sendMessage(CC.translate("&eYou gave a &4" + crate.getPrefix() + crate.getName() + " &eto &c" + target.getDisplayName() + "&e."));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        List<String> strings = new ArrayList<>();
        if (args.length == 3) {
            Crate.getCrates().forEach(crate -> strings.add(crate.getName().replace(" ", "")));
        }
        return BukkitUtils.getCompletions(args, strings);
    }
}