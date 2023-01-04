package dev.soysix.net.utils.cooldown;

import dev.soysix.net.events.CustomEvent;
import dev.soysix.net.events.PlayerBase;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class CooldownExpiredEvent extends PlayerBase {

    private Cooldown cooldown;
    private boolean forced;

    CooldownExpiredEvent(Player player, Cooldown cooldown) {
        super(player);
        this.cooldown = cooldown;
    }

    public CustomEvent setForced(boolean forced) {
        this.forced = forced;
        return this;
    }
}
