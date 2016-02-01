package mc.yqt.fixedpowerups.listeners;

import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

import mc.yqt.fixedpowerups.powerups.witherwarrior.RideableWither;

public class EntityEvents implements Listener {

	@EventHandler
	public void onDismount(EntityDismountEvent e) {

		//dismounting the wither warrior wither
		if(e.getDismounted() instanceof Wither)
			RideableWither.dismountEvent(e);
	}
}
