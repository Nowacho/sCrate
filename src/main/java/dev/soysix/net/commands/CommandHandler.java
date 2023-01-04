package dev.soysix.net.commands;

import dev.soysix.net.commands.chat.CrateExecutor;
import dev.soysix.net.sCrate;
import dev.soysix.net.utils.CC;
import dev.soysix.net.utils.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

public class CommandHandler {

    private JavaPlugin javaPlugin;
    private CommandMap commandMap;

    public CommandHandler() {
        javaPlugin = sCrate.getInst();
        registerCommand(new CrateExecutor());
    }

    private void registerCommand(BaseCommand BaseCommand) {
        registerCommand(BaseCommand, null);
    }
    private void registerCommand(BaseCommand BaseCommand, String permission) {
        PluginCommand command = getCommand(BaseCommand.getName(), javaPlugin);

        command.setPermissionMessage(CC.translate("&cYou don't have permission."));

        if (permission != null) {
            command.setPermission(permission.toLowerCase());
        }

        if (BaseCommand.getDescription() != null) {
            command.setDescription(BaseCommand.getDescription());
        }

        command.setAliases(Arrays.asList(BaseCommand.getAliases()));

        command.setExecutor(BaseCommand);
        command.setTabCompleter(BaseCommand);

        if (!getCommandMap().register(BaseCommand.getName(), command)) {
            command.unregister(getCommandMap());
            getCommandMap().register(BaseCommand.getName(), command);
        }
    }

    private CommandMap getCommandMap() {
        if (commandMap != null) {
            return commandMap;
        }

        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);

            commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
        } catch (Exception ignored) {
        }

        return commandMap;
    }

    private PluginCommand getCommand(String name, Plugin owner) {
        PluginCommand command = null;

        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            command = constructor.newInstance(name, owner);
        } catch (Exception ignored) {
        }

        return command;
    }
}
