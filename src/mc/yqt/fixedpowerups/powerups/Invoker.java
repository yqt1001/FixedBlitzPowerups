package mc.yqt.fixedpowerups.powerups;


import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mc.yqt.fixedpowerups.Powerup;

public class Invoker extends Powerup {

	public Invoker() {
		
	}
	
	@Override
	public String getName() {
		return "Invoker";
	}
	
	@Override
	public List<String> getLore() {
		return null;
	}
	
	@Override
	public ItemStack getIdentifier() {
		return new ItemStack(Material.PAPER);
	}
	
	@Override
	public void powerup(Player p) {
		
	}
}
