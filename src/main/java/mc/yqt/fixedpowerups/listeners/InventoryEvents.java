package mc.yqt.fixedpowerups.listeners;

import mc.yqt.fixedpowerups.FixedPowerups;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryEvents implements Listener {

    private FixedPowerups main;

    public InventoryEvents(FixedPowerups main) {
        this.main = main;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        //if main powerup GUI
        if (e.getInventory().getName() != null && e.getInventory().getName().equals("Blitz Powerups"))
            this.main.onGUIEvent(e);
    }
}
