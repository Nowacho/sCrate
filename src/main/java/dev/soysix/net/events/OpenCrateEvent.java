package dev.soysix.net.events;

import dev.soysix.net.crate.Crate;
import dev.soysix.net.crate.CrateReward;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class OpenCrateEvent extends PlayerBase {

    private Crate crate;
    private CrateReward crateReward;

    public OpenCrateEvent(Player player, Crate crate, CrateReward crateReward) {
        super(player);
        this.crate = crate;
        this.crateReward = crateReward;
    }
}