package dev.soysix.net.events;

import dev.soysix.net.crate.Crate;
import dev.soysix.net.events.PlayerBase;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class ReceiveCrateEvent extends PlayerBase {

    private Crate crate;

    public ReceiveCrateEvent(Player player, Crate crate) {
        super(player);
        this.crate = crate;
    }
}