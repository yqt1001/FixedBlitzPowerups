package mc.yqt.fixedpowerups.powerups.witherwarrior;

import mc.yqt.fixedpowerups.FixedPowerups;
import mc.yqt.fixedpowerups.powerups.GeneratePowerup;
import mc.yqt.fixedpowerups.powerups.Powerup;
import mc.yqt.fixedpowerups.powerups.PowerupType;
import mc.yqt.fixedpowerups.powerups.witherwarrior.RideableWither.WitherTypes;
import mc.yqt.fixedpowerups.utils.Util;
import mc.yqt.fixedpowerups.utils.NMSEntities;
import mc.yqt.fixedpowerups.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@GeneratePowerup
public class WitherWarrior extends Powerup {

    private RideableWither wither;

    public WitherWarrior(Player player) {
        super(player);

        name = "Wither Warrior";
        type = PowerupType.CORE;
        icon = new ItemStack(Material.ENDER_STONE);
        lengthInSeconds = 30;
        nms(true);
        setLore(ChatColor.YELLOW + "Lets you spawn and ride your own wither for 30",
	        ChatColor.YELLOW + "seconds! Three different types of withers that",
	        ChatColor.YELLOW + "you have an equally random chance of getting:",
	        ChatColor.GREEN + "Peaceful Wither" + ChatColor.YELLOW + ": Gives players you hit regeneration!",
	        ChatColor.GREEN + "Angry Wither" + ChatColor.YELLOW + ": Deals insane knockback!",
	        ChatColor.GREEN + "Deadly Wither" + ChatColor.YELLOW + ": A regular wither!");
    }

    @Override
    public void powerupActivate() {
        try {
            //get random wither type
            WitherTypes type = WitherTypes.values()[Util.randInt(WitherTypes.values().length)];

            //spawn wither and set player as passenger
            this.wither = (RideableWither) NMSEntities.spawnEntity(new RideableWither(this.main, player.getWorld(), type), Util.getSurface(player.getLocation()));
            this.wither.setPassenger(player);
            this.wither.setHealth(type.getType().getMaxHealth());

            //broadcast
            String s = ChatColor.YELLOW + player.getName() + " has spawned a" + ((type == WitherTypes.ANGRY) ? "n" : "") + " " + type.getType().getDisplayName() + " WITHER" + ChatColor.YELLOW + "!";
            Bukkit.broadcastMessage(s);
            Title.createTitle("", s, 80, 10, 10, Bukkit.getOnlinePlayers());

            //play sound
            player.getWorld().playSound(player.getLocation(), Sound.WITHER_SPAWN, 100, 1);

            //set the wither dismount EID in the protocol listener
            main.getListeners().getProtocolListener().setWitherEID(this.wither.getBukkitEntity().getEntityId());

        } catch (Exception e) {
            e.printStackTrace();
            FixedPowerups.setNMSState(false);
        }
    }

    @Override
    public void powerupShutdown() {
    	if(!wither.getPassenger().equals(player))
    		return;
    	
        //shut off the powerup
        wither.die();
        player.teleport(Util.getSurface(player.getLocation()));

        //remove wither skulls
        for(Entity e : player.getWorld().getEntities()) {
            if (e instanceof HarmlessWitherSkull)
                e.remove();
        }

        //reset the protocol listener EID
        this.main.getListeners().getProtocolListener().setWitherEID(0);
    }
    
    @Override
    public boolean powerupValidate() {
    	// always true
    	return true;
    }
    
    @Override
    public void playerQuit(Player player) {
    	// if the player riding the wither quits
    	if(!player.equals(this.player))
    		return;
    	
    	// remove the wither
    	wither.getBukkitEntity().remove();
    	
    	stop();
    }

    /**
     * Gets the wither spawned by this powerup
     *
     * @return
     */
    public RideableWither getWither() {
        return this.wither;
    }

}
