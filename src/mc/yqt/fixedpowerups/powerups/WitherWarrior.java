package mc.yqt.fixedpowerups.powerups;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import mc.yqt.fixedpowerups.FixedPowerups;
import mc.yqt.fixedpowerups.Powerup;
import mc.yqt.fixedpowerups.powerups.witherwarrior.RideableWither;
import mc.yqt.fixedpowerups.utils.NMSEntities;

public class WitherWarrior extends Powerup {

	private RideableWither wither;
	
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
		try {
			//spawn wither and set player as passenger
			this.wither = (RideableWither) NMSEntities.spawnEntity(new RideableWither(p.getWorld()), p.getLocation());
			this.wither.setPassenger(p);
		} catch(Exception e) {
			e.printStackTrace();
			FixedPowerups.setNMSState(false);
		}
		
		new BukkitRunnable() {
			@Override
			public void run() {
				p.sendMessage("Removing wither");
				wither.die();
			}
		}.runTaskLater(FixedPowerups.getThis(), 300l);
	}

}
