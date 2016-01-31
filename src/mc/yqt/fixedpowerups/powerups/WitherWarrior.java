package mc.yqt.fixedpowerups.powerups;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mc.yqt.fixedpowerups.Powerup;

public class WitherWarrior extends Powerup {

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
	public void powerup(Player p) {
		// TODO Auto-generated method stub
		
	}

}
