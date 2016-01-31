package mc.yqt.fixedpowerups.powerups;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import mc.yqt.fixedpowerups.FixedPowerups;
import mc.yqt.fixedpowerups.Powerup;
import mc.yqt.fixedpowerups.powerups.witherwarrior.RideableWither;
import mc.yqt.fixedpowerups.utils.NMSEntities;

public class WitherWarrior extends Powerup {

	private Entity wither;
	
	public WitherWarrior() {
		
	}
	
	@Override
	public String getName() {
		return "Wither Warrior";
	}

	@Override
	public List<String> getLore() {
		return null;
	}

	@Override
	public ItemStack getIdentifier() {
		return new ItemStack(Material.ENDER_STONE);
	}
	
	@Override
	public boolean requiresNMS() {
		return true;
	}

	@Override
	public void powerup(final Player p) {
		this.wither = (Entity) NMSEntities.spawnEntity(new RideableWither(p.getWorld()), p.getLocation());
		this.wither.setPassenger(p);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				p.sendMessage("Removing wither");
				wither.remove();
			}
		}.runTaskLater(FixedPowerups.getThis(), 300l);
	}

}
