package mc.yqt.fixedpowerups.powerups;



import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mc.yqt.fixedpowerups.Powerup;

public class Invoker extends Powerup {

	public Invoker() {
		super("Invoker", new ItemStack(Material.PAPER), false);
	}
	
	@Override
	public void powerup(Player p) {
		p.sendMessage("invoked invoker");
	}
}
