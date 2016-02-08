package mc.yqt.fixedpowerups.powerups.invoker;



import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mc.yqt.fixedpowerups.powerups.Powerup;

public class Invoker extends Powerup {

	public Invoker(String name) {
		super(name, new ItemStack(Material.PAPER), 0, 0, false);
	}

	@Override
	public void powerupActivate(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void powerupShutdown(Player p) {
		// TODO Auto-generated method stub
		
	}
}
