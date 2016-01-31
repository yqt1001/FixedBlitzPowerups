package mc.yqt.fixedpowerups;

import java.util.LinkedList;

import org.bukkit.inventory.ItemStack;

public abstract class Powerup {

	public abstract String getName();
	public abstract String[] getLore();
	public abstract ItemStack getIdentifier();
	
	public abstract void powerup();
	
	/* static methods to manage powerups */
	private static LinkedList<Powerup> powerups;
	
	public static Powerup getPowerup(String powerup) {
		for(Powerup p : powerups)
			if(p.getName().equals(powerup))
				return p;
		
		return null;
	}
}
