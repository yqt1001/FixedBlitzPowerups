package mc.yqt.fixedpowerups.powerups.witherwarrior;

import mc.yqt.fixedpowerups.FixedPowerups;
import mc.yqt.fixedpowerups.powerups.Powerup;
import mc.yqt.fixedpowerups.powerups.witherwarrior.RideableWither.WitherTypes;
import mc.yqt.fixedpowerups.utils.MiscUtils;
import mc.yqt.fixedpowerups.utils.NMSEntities;
import mc.yqt.fixedpowerups.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.Random;

public class WitherWarrior extends Powerup {

    private RideableWither wither;
    private FixedPowerups main;

    public WitherWarrior(FixedPowerups main, String name) {
        super(main, name, new ItemStack(Material.ENDER_STONE), 30, 0, true);
        this.main = main;

        LinkedList<String> lore = new LinkedList<String>();
        lore.add(ChatColor.YELLOW + "Lets you spawn and ride your own wither for 30");
        lore.add(ChatColor.YELLOW + "seconds! Three different types of withers that");
        lore.add(ChatColor.YELLOW + "you have an equally random chance of getting:");
        lore.add(ChatColor.GREEN + "Peaceful Wither" + ChatColor.YELLOW + ": Gives players you hit regeneration!");
        lore.add(ChatColor.GREEN + "Angry Wither" + ChatColor.YELLOW + ": Deals insane knockback!");
        lore.add(ChatColor.GREEN + "Deadly Wither" + ChatColor.YELLOW + ": A regular wither!");

        this.setLore(lore);
    }

    @Override
    public void powerupActivate(Player p) {
        try {
            //get random wither type
            WitherTypes type = WitherTypes.values()[new Random().nextInt(WitherTypes.values().length)];

            //spawn wither and set player as passenger
            this.wither = (RideableWither) NMSEntities.spawnEntity(new RideableWither(this.main, p.getWorld(), type), MiscUtils.getSurface(p.getLocation()));
            this.wither.setPassenger(p);
            this.wither.setHealth(type.getType().getMaxHealth());

            //broadcast
            String s = ChatColor.YELLOW + p.getName() + " has spawned a" + ((type == WitherTypes.ANGRY) ? "n" : "") + " " + type.getType().getDisplayName() + " WITHER" + ChatColor.YELLOW + "!";
            Bukkit.broadcastMessage(s);
            Title.createTitle("", s, 80, 10, 10, Bukkit.getOnlinePlayers());

            //play sound
            p.getWorld().playSound(p.getLocation(), Sound.WITHER_SPAWN, 100, 1);

            //set the wither dismount EID in the protocol listener
            this.main.getListeners().getProtocolListener().setWitherEID(this.wither.getBukkitEntity().getEntityId());

        } catch (Exception e) {
            e.printStackTrace();
            FixedPowerups.setNMSState(false);
        }
    }

    @Override
    public void powerupShutdown(Player p) {
        //shut off the powerup
        wither.die();
        p.teleport(MiscUtils.getSurface(p.getLocation()));

        //remove wither skulls
        for (Entity e : p.getWorld().getEntities()) {
            if (e instanceof HarmlessWitherSkull)
                e.remove();
        }

        //reset the protocol listener EID
        this.main.getListeners().getProtocolListener().setWitherEID(0);
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
