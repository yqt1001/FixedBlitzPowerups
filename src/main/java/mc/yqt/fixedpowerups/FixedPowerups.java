package mc.yqt.fixedpowerups;

import mc.yqt.fixedpowerups.listeners.ListenerManager;
import mc.yqt.fixedpowerups.powerups.Powerup;
import mc.yqt.fixedpowerups.powerups.PowerupList;
import mc.yqt.fixedpowerups.powerups.PowerupType;
import mc.yqt.fixedpowerups.powerups.PowerupWrapper;
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

public class FixedPowerups extends JavaPlugin {

    /**
     * God variable that is used to ensure that if any NMS errors occur at any point,
     * shut down all NMS features
     */
    private static boolean NMSenabled = true;

    private ListenerManager listeners;
    private PowerupList list;
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
        // simple NMS version checker to avoid obvious NMS errors
        if (!(Bukkit.getServer().getClass().getPackage().getName()).equals("org.bukkit.craftbukkit.v1_8_R3"))
            NMSenabled = false;

        // load powerups
        list = new PowerupList(this);

        // create listeners
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
     * @return The list of generated powerups
     */
    public PowerupList list() {
    	return list;
    }

    /**
     * Opens up powerup GUI for specified player
     * @param player
     */
    public void openPowerupGUI(Player p) {
        Inventory i = Bukkit.createInventory(p, 27, "Blitz Powerups");

        int index = 10;
        
        for(PowerupWrapper pu : list.type(PowerupType.CORE)) {
            //format item
            ItemStack is = pu.getIcon();
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ChatColor.GREEN + pu.getName());
            im.setLore(pu.getDescription());
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

        if(!(e.getWhoClicked() instanceof Player))
        	return;
        if(e.getCurrentItem() == null) 
        	return;
        if(e.getCurrentItem().getItemMeta() == null) 
        	return;
        if(e.getCurrentItem().getItemMeta().getDisplayName() == null) 
        	return;

        //remove formatting
        String s = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

        //search for the specified powerup
        PowerupWrapper p;
        if((p = list.get(s)) == null)
            return;

        //activate powerup
        p.launch((Player) e.getWhoClicked());

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
    public PowerupWrapper getPowerup(String name) {
    	return list.get(name);
    }
}
