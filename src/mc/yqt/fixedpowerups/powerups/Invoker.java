package mc.yqt.fixedpowerups.powerups;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import mc.yqt.fixedpowerups.Powerup;

public class Invoker extends Powerup {

	@Override
	public String getName() {
		return "Invoker";
	}
	
	@Override
	public String[] getLore() {
		return null;
	}
	
	@Override
	public ItemStack getIdentifier() {
		return new ItemStack(Material.PAPER);
	}
	
	@Override
	public void powerup() {
		
	}
}
