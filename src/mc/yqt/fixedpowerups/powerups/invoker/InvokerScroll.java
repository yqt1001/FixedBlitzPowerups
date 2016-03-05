package mc.yqt.fixedpowerups.powerups.invoker;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import mc.yqt.fixedpowerups.FixedPowerups;
import mc.yqt.fixedpowerups.powerups.Powerup;

public abstract class InvokerScroll extends Powerup {

	public InvokerScroll(FixedPowerups main, String s1, int length, int runtimeDelay, boolean nms) {
		super(main, s1, new ItemStack(Material.PAPER), length, runtimeDelay, nms);
	}
}
