package dev.soysix.net.commands.chat.arguments;

import com.google.common.primitives.Ints;
import dev.soysix.net.crate.Crate;
import dev.soysix.net.utils.BukkitUtils;
import dev.soysix.net.utils.CC;
import dev.soysix.net.utils.command.CommandArgument;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CrateGiveAllArgument extends CommandArgument {

    public CrateGiveAllArgument() {
        super("giveall");
    }

    @Override
    public String getUsage(String s) {
        return '/' + s + ' ' + getName() + " <crateName> <amount>";
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cUsage: " + getUsage(label)));
        } else {
            Crate crate = Crate.getByName(args[1]);
            if (crate == null) {
                sender.sendMessage(CC.translate("&cA crate with that name was not found."));
                return;
            }
            Integer amount = null;
            try {
                amount = Ints.tryParse(args[2]);
            } catch (Exception ignored) {
                sender.sendMessage(CC.translate("&cInvalid amount."));
                return;
            }
            if (amount < 1) {
                sender.sendMessage(CC.translate("&cAmount must be more than 0."));
                return;
            }
            sender.sendMessage(CC.translate("&eYou gave a &4" + crate.getPrefix() + crate.getName() + " &eto all players online."));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        List<String> strings = new ArrayList<>();
        if (args.length == 2) {
            Crate.getCrates().forEach(crate -> strings.add(crate.getName().replace(" ", "")));
        }
        return BukkitUtils.getCompletions(args, strings);
    }
}