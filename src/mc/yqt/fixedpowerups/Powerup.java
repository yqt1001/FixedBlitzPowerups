package mc.yqt.fixedpowerups;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mc.yqt.fixedpowerups.powerups.Invoker;
import mc.yqt.fixedpowerups.powerups.WitherWarrior;

public abstract class Powerup {

	public abstract String getName();
	public abstract List<String> getLore();
	public abstract ItemStack getIdentifier();
	public abstract boolean requiresNMS();
	
	public abstract void powerup(Player p);
	
	/* static methods to manage powerups */
	private static HashMap<String, Powerup> powerups = new HashMap<String, Powerup>();
	static {
		powerups.put("Invoker", new Invoker());
		powerups.put("Wither Warrior", new WitherWarrior());
	}
	
	/**
	 * Gets a powerup given the name
	 * @param Powerup name
	 * @return
	 */
	public static Powerup getPowerup(String powerup) {
		if(powerups.containsKey(powerup))
			return powerups.get(powerup);
		
		return null;
	}
	
	/**
	 * Opens up powerup GUI for specified player
	 * @param Player
	 */
	public static void openPowerupGUI(Player p) {
		Inventory i = Bukkit.createInventory(p, 27, "Blitz Powerups");
		
		int index = 1;
		
		for(Powerup pu : powerups.values()) {
			
			//format item
			ItemStack is = pu.getIdentifier();
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§a" + pu.getName());
			im.setLore(pu.getLore());
			is.setItemMeta(im);
			
			//add item
			i.setItem(index, is);
			
			//next index
			index++;
			if(((index + 1) % 9) == 0) {
				index += 2;
			}
		}
		
		//open inventory
		p.openInventory(i);
	}
	
	/**
	 * Event handler for the powerup GUI
	 * @param Event
	 */
	public static void onGUIEvent(InventoryClickEvent e) {
		if(e.getCurrentItem() == null)
			return;
		
		if(e.getCurrentItem().getItemMeta() == null)
			return;
		
		if(e.getCurrentItem().getItemMeta().getDisplayName() != null)
		{
			
			//remove formatting
			String s = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
			
			//search for the specified powerup
			Powerup p;
			if((p = getPowerup(s)) != null) {
				//if it requires NMS, make sure it is enabled
				if(p.requiresNMS() && !FixedPowerups.getNMSState()) {
					e.getWhoClicked().sendMessage("§cNMS is disabled!");
					return;
				}
				//activate powerup
				else
					p.powerup((Player) e.getWhoClicked());
				
				
				//successful, close inventory
				e.setCancelled(true);
				e.getWhoClicked().closeInventory();
			}
		}
	}
}
