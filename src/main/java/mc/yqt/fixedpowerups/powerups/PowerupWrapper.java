package mc.yqt.fixedpowerups.powerups;

import java.lang.reflect.Constructor;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mc.yqt.fixedpowerups.FixedPowerups;

public class PowerupWrapper {

	private Powerup powerup;
	private FixedPowerups main;
	private Constructor<? extends Powerup> constructor;
	
	public PowerupWrapper(Class<? extends Powerup> clazz) throws Exception {
		constructor = clazz.getConstructor(Player.class);
		powerup = create(null);
	}
	
	private Powerup create(Player player) {
		try {
			return constructor.newInstance(player);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void launch(Player player) {
		try {
			Powerup p = create(player);
			p.main = main;
			p.launch();
		} catch(Exception e) {
			player.sendMessage(ChatColor.RED + "Something went wrong!");
			e.printStackTrace();
		}
	}
	
	/**
	 * @return powerup name
	 */
	public String getName() {
		return powerup.getName();
	}
	
	/**
	 * @return powerup description
	 */
	public List<String> getDescription() {
		return powerup.getLore();
	}
	
	/**
	 * @return powerup icon
	 */
	public ItemStack getIcon() {
		return powerup.getIcon();
	}
	
	/**
	 * @return if the powerup requires NMS code to run
	 */
	public boolean nms() {
		return powerup.requiresNMS();
	}
	
	/**
	 * @return powerup type
	 */
	public PowerupType type() {
		return powerup.getType();
	}
	
	public void setMain(FixedPowerups main) {
		this.main = main;
	}
}
