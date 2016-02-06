package mc.yqt.fixedpowerups.powerups;

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
import org.bukkit.scheduler.BukkitTask;

import mc.yqt.fixedpowerups.FixedPowerups;
import mc.yqt.fixedpowerups.powerups.invoker.Invoker;
import mc.yqt.fixedpowerups.powerups.witherwarrior.WitherWarrior;

public abstract class Powerup {
	
	private String name;
	private List<String> lore;
	private ItemStack identifier;
	private boolean reqNMS;
	
	private int lengthInSeconds;
	private int runtimeDelayInTicks;
	private BukkitTask runtime;
	
	//required powerup methods
	public abstract void powerupActivate(Player p);
	public abstract void powerupShutdown(Player p);
	
	public Powerup(String s1, ItemStack id, int length, int runtimeDelay, boolean nms) {
		this.name = s1;
		this.identifier = id;
		this.reqNMS = nms;
		this.lengthInSeconds = length;
		this.runtimeDelayInTicks = runtimeDelay;
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
	
	//if this method will not be overrided, be sure to pass 0 runtime delay through the constructor
	public void powerupRuntime(Player p) { }
	
	public void powerup(final Player p) {
		/* Main powerup method, split into three parts
		 * ACTIVATE: Runs once activated
		 * RUNTIME: Bukkit runnable that runs while the powerup is active, determined by the delay given in constructor
		 * SHUTDOWN: Bukkit runnable that deactivates the powerup
		 */
		
		//activate
		Bukkit.broadcastMessage("§e" + p.getName() + " has activated the §a§l" + this.name + " §epowerup!");
		powerupActive = true;
		//give player regen 2 for 30 seconds as standard
		p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600, 1));
		
		this.powerupActivate(p);
		
		
		//runtime runnable
		if(this.runtimeDelayInTicks > 0)
		{
			runtime = new BukkitRunnable() {
				@Override
				public void run() {
					powerupRuntime(p);
				}
			}.runTaskTimer(FixedPowerups.getThis(), this.runtimeDelayInTicks, this.runtimeDelayInTicks);
		}
		
		//shutdown runnable
		new BukkitRunnable() {
			@Override
			public void run() {
				powerupShutdown(p);
				
				if(runtime != null) {
					runtime.cancel();
					runtime = null;
				}
				
				powerupActive = false;
				
				if(lengthInSeconds > 0)
					Bukkit.broadcastMessage("§eThe §a§l" + name + " §epowerup has been disabled.");
			}
		}.runTaskLater(FixedPowerups.getThis(), this.lengthInSeconds * 20);
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
					//activate powerup
					p.powerup((Player) e.getWhoClicked());
				
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
