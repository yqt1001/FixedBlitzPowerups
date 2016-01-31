package mc.yqt.fixedpowerups;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
		return false;
	}
	
	public static Plugin getThis() {
		return Bukkit.getPluginManager().getPlugin("FixedPowerups");
	}
}
