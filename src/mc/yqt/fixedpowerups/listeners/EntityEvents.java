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
		if(e.getDismounted() instanceof Wither && this.main.getActive() != null && this.main.getActive().getName().equals("Wither Warrior"))
		{
			WitherWarrior ww = (WitherWarrior) this.main.getActive();
			ww.getWither().dismountEvent(e);
		}
	}
}
