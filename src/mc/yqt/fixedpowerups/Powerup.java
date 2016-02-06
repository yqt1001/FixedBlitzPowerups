package mc.yqt.fixedpowerups;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import mc.yqt.fixedpowerups.powerups.Invoker;
import mc.yqt.fixedpowerups.powerups.WitherWarrior;

public abstract class Powerup {
	
	private String name;
	private List<String> lore;
	private ItemStack identifier;
	private boolean reqNMS;
	
	public abstract void powerup(Player p);
	
	public Powerup(String s1, ItemStack id, boolean nms) {
		this.name = s1;
		this.identifier = id;
		this.reqNMS = nms;
		this.lore = null;
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<String> getLore() {
		return this.lore;
	}
	
	public void setLore(List<String> lore) {
		this.lore = lore;
	}
	
	public ItemStack getIdentifier() {
		return this.identifier;
	}
	
	public boolean requiresNMS() {
		return this.reqNMS;
	}
	
	/* Static methods to manage powerups */
	private static HashMap<String, Powerup> powerups = new HashMap<String, Powerup>();
	public static boolean powerupActive = false;
	
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
	public static void onGUIEvent(final InventoryClickEvent e) {
		e.setCancelled(true);
		
		if(e.getCurrentItem() == null)
			return;
		
		if(e.getCurrentItem().getItemMeta() == null)
			return;
		
		//if there is a powerup currently active, stop now
		if(powerupActive)
		{
			e.getWhoClicked().sendMessage("§cAnother powerup is currently active!");
			return;
		}
		
		if(e.getCurrentItem().getItemMeta().getDisplayName() != null)
		{
			
			//remove formatting
			String s = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
			
			//search for the specified powerup
			Powerup p;
			if((p = getPowerup(s)) != null) {
				//if it requires NMS, make sure it is enabled
				if(p.requiresNMS() && !FixedPowerups.getNMSState()) 
				{
					e.getWhoClicked().sendMessage("§cNMS is disabled!");
					return;
				}
				else 
				{
					
					//broadcast message
					Bukkit.broadcastMessage("§e" + e.getWhoClicked().getName() + " has activated the §a§l" + s + " §epowerup!");
					
					//activate powerup
					p.powerup((Player) e.getWhoClicked());
					powerupActive = true;
					
					//give player regen 2 for 30 seconds as standard
					e.getWhoClicked().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600, 1));
				}
				
				
				//successful, close inventory
				new BukkitRunnable() {
					@Override
					public void run() {
						e.getWhoClicked().closeInventory();
					}
				}.runTaskLater(FixedPowerups.getThis(), 1l);
			}
		}
	}
}
