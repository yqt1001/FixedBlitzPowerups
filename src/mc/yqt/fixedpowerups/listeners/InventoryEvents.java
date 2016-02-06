package mc.yqt.fixedpowerups.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import mc.yqt.fixedpowerups.powerups.Powerup;

public class InventoryEvents implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		
		//if main powerup GUI
		if(e.getInventory().getName() != null && e.getInventory().getName().equals("Blitz Powerups"))
			Powerup.onGUIEvent(e);
	}
}
