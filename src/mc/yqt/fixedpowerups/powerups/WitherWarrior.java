package mc.yqt.fixedpowerups.powerups;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import mc.yqt.fixedpowerups.Powerup;

public class WitherWarrior extends Powerup {

	@Override
	public String getName() {
		return "Wither Warrior";
	}

	@Override
	public String[] getLore() {
		return null;
	}

	@Override
	public ItemStack getIdentifier() {
		return new ItemStack(Material.ENDER_STONE);
	}

	@Override
	public void powerup() {
		// TODO Auto-generated method stub
		
	}

}
