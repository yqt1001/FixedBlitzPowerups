package mc.yqt.fixedpowerups;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import mc.yqt.fixedpowerups.listeners.InventoryEvents;

public class FixedPowerups extends JavaPlugin {

	@Override
	public void onEnable() {
		
		Bukkit.getPluginManager().registerEvents(new InventoryEvents(), this);
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
		}
		
		
		return false;
	}
	
	public static Plugin getThis() {
		return Bukkit.getPluginManager().getPlugin("FixedPowerups");
	}
}
