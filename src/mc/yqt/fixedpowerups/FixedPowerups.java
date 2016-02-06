package mc.yqt.fixedpowerups;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import mc.yqt.fixedpowerups.listeners.*;
import mc.yqt.fixedpowerups.powerups.Powerup;

public class FixedPowerups extends JavaPlugin {

	//god variable that is used to ensure that if any NMS errors occur at any point, shut down all NMS features
	private static boolean NMSenabled = true;
	
	@Override
	public void onEnable() {
		
		Bukkit.getPluginManager().registerEvents(new InventoryEvents(), this);
		Bukkit.getPluginManager().registerEvents(new EntityEvents(), this);
		ProtocolListeners.onEnable();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("powerup"))
		{
			//make sure sender is a player
			if(!(sender instanceof Player)) {
				sender.sendMessage("You can't do this!");
				return true;
			}
			
			Powerup.openPowerupGUI((Player) sender);
			return true;
		}
		
		
		return false;
	}
	
	public static Plugin getThis() {
		return Bukkit.getPluginManager().getPlugin("FixedPowerups");
	}
	
	/**
	 * Gets NMS enabled
	 * @return boolean
	 */
	public static boolean getNMSState() {
		return NMSenabled;
	}
	
	/**
	 * Sets NMS enabled
	 * @param New state
	 */
	public static void setNMSState(boolean newState) {
		NMSenabled = newState;
	}
}
