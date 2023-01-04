package dev.soysix.net.utils.cooldown;

import dev.soysix.net.events.PlayerBase;
import lombok.Getter;
import org.bukkit.entity.Player;

public class CooldownStartedEvent extends PlayerBase {

    @Getter
    private Cooldown cooldown;

    public CooldownStartedEvent(Player player, Cooldown cooldown) {
        super(player);
        this.cooldown = cooldown;
    }
}
