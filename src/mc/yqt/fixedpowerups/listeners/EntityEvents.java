package mc.yqt.fixedpowerups.listeners;

import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

import mc.yqt.fixedpowerups.FixedPowerups;
import mc.yqt.fixedpowerups.powerups.witherwarrior.WitherWarrior;

public class EntityEvents implements Listener {

	private FixedPowerups main;
	
	public EntityEvents(FixedPowerups main) {
		this.main = main;
	}
	
	@EventHandler
	public void onDismount(EntityDismountEvent e) {

		//dismounting the wither warrior wither
		WitherWarrior active;
		if(e.getDismounted() instanceof Wither && (active = this.main.getActive(WitherWarrior.class)) != null)
			active.getWither().dismountEvent(e);
	}
}
