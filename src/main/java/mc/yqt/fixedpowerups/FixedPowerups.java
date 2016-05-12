package mc.yqt.fixedpowerups;

import mc.yqt.fixedpowerups.listeners.ListenerManager;
import mc.yqt.fixedpowerups.powerups.Powerup;
import mc.yqt.fixedpowerups.powerups.invoker.Invoker;
import mc.yqt.fixedpowerups.powerups.witherwarrior.WitherWarrior;
import mc.yqt.fixedpowerups.utils.RunnableBuilder;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class FixedPowerups extends JavaPlugin {

    /**
     * God variable that is used to ensure that if any NMS errors occur at any point,
     * shut down all NMS features
     */
    private static boolean NMSenabled = true;

    private ListenerManager listeners;
    private HashMap<String, Powerup> powerups = new HashMap<String, Powerup>();
    private Powerup powerupActive = null;

    /**
     * @return Current NMS state
     */
    public static boolean getNMSState() {
        return NMSenabled;
    }

    /**
     * Sets NMS state
     *
     * @param newState
     */
    public static void setNMSState(boolean newState) {
        NMSenabled = newState;
    }

    @Override
    public void onEnable() {
        //simple NMS version checker to avoid obvious NMS errors
        if (!(Bukkit.getServer().getClass().getPackage().getName()).equals("org.bukkit.craftbukkit.v1_8_R3"))
            NMSenabled = false;

        //add powerups to map
        powerups.put("Invoker", new Invoker(this, "Invoker"));
        powerups.put("Wither Warrior", new WitherWarrior(this, "Wither Warrior"));

        //create listeners
        listeners = new ListenerManager(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("powerup")) {
            //make sure sender is a player
            if (!(sender instanceof Player)) {
                sender.sendMessage("You can't do this!");
                return true;
            }

            this.openPowerupGUI((Player) sender);
            return true;
        }


        return false;
    }

    /**
     * @return The listeners
     */
    public ListenerManager getListeners() {
        return this.listeners;
    }
    
    /**
     * @return Map of powerups
     */
    public HashMap<String, Powerup> getPowerups() {
    	return powerups;
    }

    /**
     * Opens up powerup GUI for specified player
     *
     * @param Player
     */
    public void openPowerupGUI(Player p) {
        Inventory i = Bukkit.createInventory(p, 27, "Blitz Powerups");

        int index = 10;

        for (Powerup pu : powerups.values()) {

            //format item
            ItemStack is = pu.getIdentifier();
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ChatColor.GREEN + pu.getName());
            im.setLore(pu.getLore());
            is.setItemMeta(im);

            //add item
            i.setItem(index, is);

            //next index
            index++;
            if (((index + 1) % 9) == 0)
                index += 2;
        }

        //open inventory
        p.openInventory(i);
    }

    /**
     * Event handler for the powerup GUI
     *
     * @param Event
     */
    public void onGUIEvent(final InventoryClickEvent e) {
        e.setCancelled(true);

        if(e.getCurrentItem() == null) 
        	return;
        if(e.getCurrentItem().getItemMeta() == null) 
        	return;
        if(e.getCurrentItem().getItemMeta().getDisplayName() == null) 
        	return;

        //if there is a powerup currently active, stop now
        if(powerupActive != null) {
            e.getWhoClicked().sendMessage(ChatColor.RED + "Another powerup is currently active!");
            return;
        }

        //remove formatting
        String s = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

        //search for the specified powerup
        Powerup p;
        if((p = powerups.get(s)) == null)
            return;

        //if it requires NMS, make sure it is enabled
        if(p.requiresNMS() && !getNMSState()) {
            e.getWhoClicked().sendMessage(ChatColor.RED + "NMS is disabled!");
            return;
        }

        //activate powerup
        p.powerup((Player) e.getWhoClicked());

        //successful, close inventory
        RunnableBuilder.make(this).run(() -> e.getWhoClicked().closeInventory());
    }

    /**
     * Gets the currently active powerup
     *
     * @return
     */
    public Powerup getActive() {
        return this.powerupActive;
    }

    /**
     * Sets a new value for an active powerup
     *
     * @param active
     */
    public void setActive(Powerup p) {
        this.powerupActive = p;
    }

    /**
     * {@link #getActive()} using generics to avoid spamming listener code with if(active instanceof PowerupType) and casts
     *
     * @param clazz
     * @return
     */
    public <P extends Powerup> P getActive(Class<P> clazz) {
        if(clazz.isInstance(this.powerupActive))
            return clazz.cast(this.powerupActive);

        return null;
    }
    
    /**
     * @param name
     * @return Specified powerup from name
     */
    public Powerup getPowerup(String name) {
    	return powerups.get(name);
    }
    
    /**
     * {@link #getPowerup(String)} using generics.
     * @param clazz
     * @return The powerup from the given class
     */
    public <P extends Powerup> P getPowerup(Class<P> clazz) {
    	for(Powerup p : powerups.values())
    		if(clazz.isInstance(p))
    			return clazz.cast(p);
    	
    	return null;
    }
}
