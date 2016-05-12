package mc.yqt.fixedpowerups.powerups.invoker;

import mc.yqt.fixedpowerups.FixedPowerups;
import mc.yqt.fixedpowerups.powerups.Powerup;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
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
     * @return A map containing all staticly defined invoker scrolls
     */
    public static Map<String, InvokerScroll> getScrolls() {
        return scrolls;
    }
    
    public void onInteract(PlayerInteractEvent e) {
    	if(!(e.getAction() == Action.RIGHT_CLICK_AIR) || !(e.getAction() == Action.RIGHT_CLICK_BLOCK)) 
        	return;
        if(e.getItem().getItemMeta() == null || e.getItem().getItemMeta().getDisplayName() == null) 
        	return;
        
        String s = e.getItem().getItemMeta().getDisplayName().substring(2);
        if(!Invoker.getScrolls().containsKey(s)) 
        	return;
        InvokerScroll is = Invoker.getScrolls().get(s);
        
        //check NMS is enabled, if scroll requires NMS
        if (is.requiresNMS() && !FixedPowerups.getNMSState()) {
            e.getPlayer().sendMessage(ChatColor.RED + "NMS is disabled!");
            return;
        }

        //activate scroll powerup
        is.powerup(e.getPlayer());

        //remove all scrolls from inventory
        for (ItemStack i : e.getPlayer().getInventory().getContents())
            if(i.getType() == Material.PAPER)
                if(i.getItemMeta() != null && i.getItemMeta().getDisplayName() != null) {
                    s = i.getItemMeta().getDisplayName().substring(2);
                    if(Invoker.getScrolls().containsKey(s))
                        i.setAmount(0);
                }
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
