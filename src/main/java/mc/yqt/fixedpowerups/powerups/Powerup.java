package mc.yqt.fixedpowerups.powerups;

import mc.yqt.fixedpowerups.FixedPowerups;
import mc.yqt.fixedpowerups.utils.MiscUtils;
import mc.yqt.fixedpowerups.utils.RunnableBuilder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public abstract class Powerup {

    private String name;
    private List<String> lore;
    private ItemStack identifier;
    private boolean reqNMS;
    private boolean canceled;

    private int lengthInSeconds;
    private int runtimeDelayInTicks;
    private BukkitTask runtime;

    private FixedPowerups main;

    public Powerup(FixedPowerups main, String s1, ItemStack id, int length, int runtimeDelay, boolean nms) {
        this.main = main;
        this.name = s1;
        this.identifier = id;
        this.reqNMS = nms;
        this.canceled = false;
        this.lengthInSeconds = length;
        this.runtimeDelayInTicks = runtimeDelay;
        this.lore = null;
    }

    //required powerup methods
    public abstract void powerupActivate(Player p);
    public abstract void powerupShutdown(Player p);

    public String getName() {
        return this.name;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public ItemStack getIdentifier() {
        return this.identifier;
    }

    public boolean requiresNMS() {
        return this.reqNMS;
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public void cancel() {
        this.canceled = true;
        if (this.runtime != null) this.runtime.cancel();
        this.runtime = null;
        this.main.setActive(null);
    }

    //if this method will not be overwritten, be sure to pass 0 runtime delay through the constructor
    public void powerupRuntime(Player p) {
    }

    public void powerup(final Player p) {
        /* Main powerup method, split into three parts
		 * ACTIVATE: Runs once activated
		 * RUNTIME: Bukkit runnable that runs while the powerup is active, determined by the delay given in constructor
		 * SHUTDOWN: Bukkit runnable that deactivates the powerup
		 */

        //activate
        Bukkit.broadcastMessage(MiscUtils.modulate(p.getName() + " has activated the" + MiscUtils.encapsulate(this.name) + " powerup!"));
        this.main.setActive(this);
        //give player regen 2 for 30 seconds as standard
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600, 1), true);

        this.powerupActivate(p);


        //runtime runnable
        if (this.runtimeDelayInTicks > 0)
        	runtime = RunnableBuilder.make(main).delay(runtimeDelayInTicks).run(() -> powerupRuntime(p));


        //shutdown runnable
        RunnableBuilder.make(main).delay(lengthInSeconds * 20).run(() -> {
        	if(canceled)
                return;

            powerupShutdown(p);

            if(runtime != null) {
                runtime.cancel();
                runtime = null;
            }

            main.setActive(null);

            if(lengthInSeconds > 0)
                Bukkit.broadcastMessage(MiscUtils.modulate("The " + MiscUtils.encapsulate(name) + " powerup has been disabled."));
        });
    }
}
