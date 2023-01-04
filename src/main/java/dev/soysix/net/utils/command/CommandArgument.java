package dev.soysix.net.utils.command;

import dev.soysix.net.sCrate;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class CommandArgument {

    public sCrate scrate = sCrate.getInst();

    @Getter
    @Setter
    public String name, description, permission;
    public String[] aliases;
    @Getter
    @Setter
    public boolean onlyplayers;

    public CommandArgument(String name) {
        this(name, null);
    }

    public CommandArgument(String name, String description) {
        this(name, description, null);
    }

    public CommandArgument(String name, String description, String permission) {
        this(name, description, permission, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public CommandArgument(String name, String description, String permission, String... aliases) {
        this(name, description, permission, aliases, false);
    }

    public CommandArgument(String name, String description, String permission, String[] aliases, boolean onlyplayers) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.aliases = Arrays.copyOf(aliases, aliases.length);
        this.onlyplayers = onlyplayers;
    }

    public abstract String getUsage(String label);

    public abstract void onExecute(CommandSender sender , String label, String[] args);

    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        return Collections.emptyList();
    }
}