package mc.yqt.fixedpowerups.powerups.invoker;

import mc.yqt.fixedpowerups.powerups.Powerup;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class InvokerScroll extends Powerup {

    public InvokerScroll(Player player) {
        super(player);
        icon = new ItemStack(Material.PAPER);
    }
}
