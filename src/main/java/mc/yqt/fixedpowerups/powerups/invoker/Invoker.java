package mc.yqt.fixedpowerups.powerups.invoker;

import mc.yqt.fixedpowerups.FixedPowerups;
import mc.yqt.fixedpowerups.powerups.Powerup;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class Invoker extends Powerup {

    private static Map<String, InvokerScroll> scrolls = new HashMap<>();

    public Invoker(FixedPowerups main, String name) {
        super(main, name, new ItemStack(Material.PAPER), 0, 0, false);

        scrolls.put("Death Curse", new DeathScroll(main));
    }

    /**
     * @return Returns a map containing all staticly defined invoker scrolls
     */
    public static Map<String, InvokerScroll> getScrolls() {
        return scrolls;
    }

    @Override
    public void powerupActivate(Player p) {
        for (InvokerScroll is : scrolls.values()) {
            ItemStack i = is.getIdentifier();
            ItemMeta im = i.getItemMeta();
            im.setDisplayName(ChatColor.GREEN + is.getName());
            im.setLore(is.getLore());
            i.setItemMeta(im);

            p.getInventory().addItem(i);
        }
    }

    @Override
    public void powerupShutdown(Player p) {
        // do literally nothing
        return;
    }
}
