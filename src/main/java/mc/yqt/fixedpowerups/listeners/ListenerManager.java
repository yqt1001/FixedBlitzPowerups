package mc.yqt.fixedpowerups.listeners;

import mc.yqt.fixedpowerups.FixedPowerups;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class ListenerManager {

	/* Object to manage listeners */

    private Listener inventory;
    private Listener entity;
    private ProtocolListeners protocol;

    public ListenerManager(FixedPowerups main) {
        inventory = new InventoryEvents(main);
        entity = new EntityEvents(main);
        protocol = new ProtocolListeners(main);

        Bukkit.getPluginManager().registerEvents(inventory, main);
        Bukkit.getPluginManager().registerEvents(entity, main);
    }

    public Listener getInventoryListener() {
        return this.inventory;
    }

    public Listener getEntityListener() {
        return this.entity;
    }

    public ProtocolListeners getProtocolListener() {
        return this.protocol;
    }
}
