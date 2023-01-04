package dev.soysix.net;

import dev.soysix.net.commands.CommandHandler;
import dev.soysix.net.listener.CrateListener;
import dev.soysix.net.utils.CC;
import dev.soysix.net.utils.cooldown.Cooldown;
import dev.soysix.net.utils.TimeUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Setter
public final class sCrate extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getServer().getConsoleSender().sendMessage(CC.translate("&b[sCrate] &ahas been registered"));
        this.registerListeners();
        this.registerManagers();
        this.registerCooldowns();

        new CommandHandler();
    }

    private void registerCooldowns() {
        new Cooldown("THOR_AXE", TimeUtils.parse("20s"));
        new Cooldown("THROWABLE_COBWEB", TimeUtils.parse("2m"));
    }

    private void registerListeners() {
        PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents(new CrateListener(), this);
    }

    @Override
    public void onDisable() {
    }

    private void registerManagers() {
        new CommandHandler();
    }

    public static sCrate getInst() {
        return sCrate.getPlugin(sCrate.class);
    }
}
