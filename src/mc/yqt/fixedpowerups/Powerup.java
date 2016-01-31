package mc.yqt.fixedpowerups;

import java.util.LinkedList;

import org.bukkit.inventory.ItemStack;

abstract class Powerup {

	abstract String getName();
	abstract String[] getLore();
	abstract ItemStack getIdentifier();
	
	abstract void powerup();
	
	/* static methods to manage powerups */
	private static LinkedList<Powerup> powerups;
	
	public static Powerup getPowerup(String powerup) {
		for(Powerup p : powerups)
			if(p.getName().equals(powerup))
				return p;
		
		return null;
	}
}
