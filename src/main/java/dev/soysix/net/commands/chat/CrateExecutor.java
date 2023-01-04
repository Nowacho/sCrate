package dev.soysix.net.commands.chat;

import dev.soysix.net.commands.chat.arguments.CrateGiveAllArgument;
import dev.soysix.net.commands.chat.arguments.CrateGiveArgument;
import dev.soysix.net.crate.Crate;
import dev.soysix.net.utils.CC;
import dev.soysix.net.utils.command.CommandArgument;
import dev.soysix.net.utils.command.BaseCommand;
import org.bukkit.command.CommandSender;

public class CrateExecutor extends BaseCommand {

    public CrateExecutor() {
        super("crate");
        registerArgument(new CrateGiveAllArgument());
        registerArgument(new CrateGiveArgument());
        registerArgument(new CommandArgument("list") {
            @Override
            public String getUsage(String label) {
                return '/' + label + ' ' + getName();
            }

            @Override
            public void onExecute(CommandSender sender, String label, String[] args) {
                if (Crate.getCrates().isEmpty()) {
                    sender.sendMessage(CC.translate("&cThere are no crates created."));
                } else {
                    sender.sendMessage(CC.translate("&aCrates &7(&e" + Crate.getCrates().size() + "&7)"));
                    Crate.getCrates().forEach(crate -> sender.sendMessage(CC.translate(" &8- &c" + crate.getName())));
                }
            }
        });
    }
}
