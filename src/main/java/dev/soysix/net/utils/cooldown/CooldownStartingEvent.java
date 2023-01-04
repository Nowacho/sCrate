package dev.soysix.net.utils.cooldown;

import dev.soysix.net.events.PlayerBase;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
public class CooldownStartingEvent extends PlayerBase {

    private Cooldown cooldown;
    @Setter
    private String reason;

    public CooldownStartingEvent(Player player, Cooldown cooldown) {
        super(player);
        this.cooldown = cooldown;
    }

}
