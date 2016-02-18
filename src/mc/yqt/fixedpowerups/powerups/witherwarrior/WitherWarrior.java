package mc.yqt.fixedpowerups.powerups.witherwarrior;

import java.util.LinkedList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mc.yqt.fixedpowerups.FixedPowerups;
import mc.yqt.fixedpowerups.powerups.Powerup;
import mc.yqt.fixedpowerups.powerups.witherwarrior.RideableWither.WitherTypes;
import mc.yqt.fixedpowerups.utils.MiscUtils;
import mc.yqt.fixedpowerups.utils.NMSEntities;
import mc.yqt.fixedpowerups.utils.Title;

public class WitherWarrior extends Powerup {

	private RideableWither wither;
	private FixedPowerups main;
	
	public WitherWarrior(FixedPowerups main, String name) {
		super(main, name, new ItemStack(Material.ENDER_STONE), 30, 0, true);
		this.main = main;
		
		LinkedList<String> lore = new LinkedList<String>();
		lore.add("§eLets you spawn and ride your own wither for 30");
		lore.add("§eseconds! Three different types of withers that");
		lore.add("§eyou have an equally random chance of getting:");
		lore.add("§aPeaceful Wither§e: Gives players you hit regeneration!");
		lore.add("§aAngry Wither§e: Deals insane knockback!");
		lore.add("§aDeadly Wither§e: A regular wither!");
		
		this.setLore(lore);
	}

	@Override
	public void powerupActivate(Player p) {
		try {
			//get random wither type
			WitherTypes type = WitherTypes.values()[new Random().nextInt(WitherTypes.values().length)];
			
			//spawn wither and set player as passenger
			this.wither = (RideableWither) NMSEntities.spawnEntity(new RideableWither(this.main, p.getWorld(), type), MiscUtils.getSurface(p.getLocation()));
			this.wither.setPassenger(p);
			this.wither.setHealth(type.getType().getMaxHealth());
			
			//broadcast
			String s = "§e" + p.getName() + " has spawned a" + ((type == WitherTypes.ANGRY) ? "n" : "") + " " + type.getType().getDisplayName() + " WITHER§e!";
			Bukkit.broadcastMessage(s);
			Title.createTitle("", s, 80, 10, 10, Bukkit.getOnlinePlayers());
			
			//play sound
			p.getWorld().playSound(p.getLocation(), Sound.WITHER_SPAWN, 100, 1);
			
			//set the wither dismount EID in the protocol listener
			this.main.getListeners().getProtocolListener().setWitherEID(this.wither.getBukkitEntity().getEntityId());
			
		} catch(Exception e) {
			e.printStackTrace();
			FixedPowerups.setNMSState(false);
		}
	}
	
	@Override
	public void powerupShutdown(Player p) {
		//shut off the powerup
		wither.die();
		p.teleport(MiscUtils.getSurface(p.getLocation()));
		
		//remove wither skulls
		for(Entity e : p.getWorld().getEntities()) {
			if(e instanceof HarmlessWitherSkull)
				e.remove();
		}
		
		//reset the protocol listener EID
		this.main.getListeners().getProtocolListener().setWitherEID(0);
	}
	
	/**
	 * Gets the wither spawned by this powerup
	 * @return
	 */
	public RideableWither getWither() {
		return this.wither;
	}

}
