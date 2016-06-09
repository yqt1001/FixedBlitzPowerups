package mc.yqt.fixedpowerups.listeners;

import mc.yqt.fixedpowerups.FixedPowerups;
import mc.yqt.fixedpowerups.powerups.invoker.DeathScroll;
import mc.yqt.fixedpowerups.powerups.invoker.Invoker;
import mc.yqt.fixedpowerups.powerups.witherwarrior.WitherWarrior;
import org.bukkit.Material;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class EntityEvents implements Listener {

    private FixedPowerups main;

    public EntityEvents(FixedPowerups main) {
        this.main = main;
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
    	
    	// run player quit event for active powerup if there is one
    	if(main.getActive() != null)
    		main.getActive().playerQuit(e.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        //invoker scroll interact
        if(e.hasItem() && e.getItem().getType() == Material.PAPER)
        	Invoker.onInteract(main, e);
    }
    
    @EventHandler
    public void onEntityAttacked(EntityDamageByEntityEvent e) {
    	// death scroll damage insta-kill
    	DeathScroll active;
    	if((active = main.getActive(DeathScroll.class)) != null)
    		active.onDamage(e);
    }

    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        WitherWarrior active;
        if(e.getDismounted() instanceof Wither && (active = this.main.getActive(WitherWarrior.class)) != null)
            active.getWither().dismountEvent(e);
    }
}
