package mc.yqt.fixedpowerups.powerups;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import mc.yqt.fixedpowerups.FixedPowerups;

public abstract class Powerup {
	
	private String name;
	private List<String> lore;
	private ItemStack identifier;
	private boolean reqNMS;
	
	private int lengthInSeconds;
	private int runtimeDelayInTicks;
	private BukkitTask runtime;
	
	private FixedPowerups main;
	
	//required powerup methods
	public abstract void powerupActivate(Player p);
	public abstract void powerupShutdown(Player p);
	
	public Powerup(FixedPowerups main, String s1, ItemStack id, int length, int runtimeDelay, boolean nms) {
		this.main = main;
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
	
	//if this method will not be overwritten, be sure to pass 0 runtime delay through the constructor
	public void powerupRuntime(Player p) { }
	
	public void powerup(final Player p) {
		/* Main powerup method, split into three parts
		 * ACTIVATE: Runs once activated
		 * RUNTIME: Bukkit runnable that runs while the powerup is active, determined by the delay given in constructor
		 * SHUTDOWN: Bukkit runnable that deactivates the powerup
		 */
		
		//activate
		Bukkit.broadcastMessage("§e" + p.getName() + " has activated the §a§l" + this.name + " §epowerup!");
		this.main.setActive(this);
		//give player regen 2 for 30 seconds as standard
		p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600, 1));
		
		this.powerupActivate(p);
		
		
		//runtime runnable
		if(this.runtimeDelayInTicks > 0)
			runtime = new BukkitRunnable() {
				@Override
				public void run() {
					powerupRuntime(p);
				}
			}.runTaskTimer(main, this.runtimeDelayInTicks, this.runtimeDelayInTicks);
		
		
		//shutdown runnable
		new BukkitRunnable() {
			@Override
			public void run() {
				powerupShutdown(p);
				
				if(runtime != null) {
					runtime.cancel();
					runtime = null;
				}
				
				main.setActive(null);
				
				if(lengthInSeconds > 0)
					Bukkit.broadcastMessage("§eThe §a§l" + name + " §epowerup has been disabled.");
			}
		}.runTaskLater(main, this.lengthInSeconds * 20);
	}
}
