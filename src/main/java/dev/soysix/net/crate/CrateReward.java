package dev.soysix.net.crate;

import dev.soysix.net.utils.Callback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class CrateReward {

    private ItemStack itemStack;
    private Callback<Player> callback;

    private int chance;
}