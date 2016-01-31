package mc.yqt.fixedpowerups;

import java.util.Arrays;
import java.util.LinkedList;
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
	
	public abstract void powerup(Player p);
	
	/* static methods to manage powerups */
	private static LinkedList<Powerup> powerups = new LinkedList<Powerup>(Arrays.asList(new Invoker(), new WitherWarrior()));
	
	/**
	 * Gets a powerup given the name
	 * @param Powerup name
	 * @return
	 */
	public static Powerup getPowerup(String powerup) {
		for(Powerup p : powerups)
			if(p.getName().equals(powerup))
				return p;
		
		return null;
	}
	
	/**
	 * Opens up powerup GUI for specified player
	 * @param Player
	 */
	public static void openPowerupGUI(Player p) {
		Inventory i = Bukkit.createInventory(p, 27, "Blitz Powerups");
		
		int index = 2;
		
		for(Powerup pu : powerups) {
			
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
			if((p = getPowerup(s)) != null)
				//activate powerup
				p.powerup((Player) e.getWhoClicked());
			
		}
	}
}
