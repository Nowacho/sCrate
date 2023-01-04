package dev.soysix.net.utils.command;

import dev.soysix.net.sCrate;
import dev.soysix.net.utils.BukkitUtils;
import dev.soysix.net.utils.CC;
import lombok.Getter;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseCommand implements CommandExecutor, TabCompleter {

    public sCrate scrate = sCrate.getInst();

    private String name, description;
    private String[] aliases;

    @Getter
    private List<CommandArgument> commandArgument = new ArrayList<>();

    public BaseCommand(String name) {
        this(name, null);
    }

    public BaseCommand(String name, String description) {
        this(name, description, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public BaseCommand(String name, String description, String... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = Arrays.copyOf(aliases, aliases.length);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate("&7&m" + StringUtils.repeat("-", 30)));
            sender.sendMessage(CC.translate("&7Available sub-command(s) for &c" + command.getName() + "&7."));
            sender.sendMessage("");

            for (CommandArgument commandArgument : commandArgument) {
                if (commandArgument.permission != null && !sender.hasPermission(commandArgument.permission)) {
                    continue;
                }
                sender.sendMessage(CC.translate(" &c" + commandArgument.getUsage(label) + (commandArgument.description != null ? " &8- &f" + commandArgument.description : "")));
            }
            sender.sendMessage(CC.translate("&7&m" + StringUtils.repeat("-", 30)));
        } else {
            CommandArgument commandArgument = getArgument(args[0]);
            if (commandArgument == null || (commandArgument.permission != null && !sender.hasPermission(commandArgument.permission))) {
                sender.sendMessage(CC.translate("&cNo argument found."));
            } else {
                if (commandArgument.onlyplayers && sender instanceof ConsoleCommandSender) {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&cPlayer is currently offline."));
                    return false;
                }
                commandArgument.onExecute(sender, label, args);
            }
        }
        return true;
    }

    public CommandArgument getArgument(String name) {
        for (CommandArgument commandArgument : commandArgument) {
            if (commandArgument.name.equalsIgnoreCase(name) || Arrays.asList(commandArgument.aliases).contains(name.toLowerCase())) {
                return commandArgument;
            }
        }
        return null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> results = new ArrayList<>();
        if (args.length < 2) {

            for (CommandArgument commandArgument : commandArgument) {
                String permission = commandArgument.permission;
                if (permission == null || sender.hasPermission(permission)) {
                    results.add(commandArgument.name);
                }
            }

            if (results.isEmpty()) {
                return null;
            }
        } else {
            CommandArgument commandArgument = getArgument(args[0]);
            if (commandArgument == null) {
                return results;
            }

            String permission = commandArgument.permission;
            if (permission == null || sender.hasPermission(permission)) {
                results = commandArgument.onTabComplete(sender, label, args);

                if (results == null) {
                    return null;
                }
            }
        }

        return BukkitUtils.getCompletions(args, results);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void registerArgument(CommandArgument commandArgument) {
        this.commandArgument.add(commandArgument);
    }

}