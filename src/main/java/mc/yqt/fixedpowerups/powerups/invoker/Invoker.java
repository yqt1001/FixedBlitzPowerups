package mc.yqt.fixedpowerups.powerups.invoker;

import mc.yqt.fixedpowerups.FixedPowerups;
import mc.yqt.fixedpowerups.powerups.GeneratePowerup;
import mc.yqt.fixedpowerups.powerups.Powerup;
import mc.yqt.fixedpowerups.powerups.PowerupType;
import mc.yqt.fixedpowerups.powerups.PowerupWrapper;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@GeneratePowerup
public class Invoker extends Powerup {

    public Invoker(Player player) {
        super(player);

        name = "Invoker";
        type = PowerupType.CORE;
        icon = new ItemStack(Material.PAPER);
        lengthInSeconds = 0;
    }

    @Override
    public void powerupActivate() {
        for(PowerupWrapper is : main.list().type(PowerupType.INVOKER)) {
            ItemStack i = is.getIcon();
            ItemMeta im = i.getItemMeta();
            im.setDisplayName(ChatColor.GREEN + is.getName());
            im.setLore(is.getDescription());
            i.setItemMeta(im);

            player.getInventory().addItem(i);
        }
    }

    @Override
    public void powerupShutdown() {
        // do literally nothing
        return;
    }

	@Override
	public boolean powerupValidate() {
		// always run
		return true;
	}
	
	public static void onInteract(FixedPowerups main, PlayerInteractEvent e) {
    	if(!(e.getAction() == Action.RIGHT_CLICK_AIR) && !(e.getAction() == Action.RIGHT_CLICK_BLOCK)) 
        	return;
        if(e.getItem().getItemMeta() == null || e.getItem().getItemMeta().getDisplayName() == null) 
        	return;
        
        String s = e.getItem().getItemMeta().getDisplayName().substring(2);
        PowerupWrapper p;
        if((p = main.getPowerup(s)) == null) 
        	return;

        // activate scroll powerup
        if(!p.launch(e.getPlayer()))
        	return;

        // remove all scrolls from inventory if the powerup launched correctly
        Inventory inv = e.getPlayer().getInventory();
        for(int i = 0; i < 36; i++) {
        	ItemStack item = inv.getItem(i);
            if(item != null && item.getType() == Material.PAPER)
                if(item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null) {
                    s = item.getItemMeta().getDisplayName().substring(2);
                    if(main.getPowerup(s) != null)
                        inv.setItem(i, new ItemStack(Material.AIR));
                }
        }
    }
}
