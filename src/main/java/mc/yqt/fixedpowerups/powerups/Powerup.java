package mc.yqt.fixedpowerups.powerups;

import mc.yqt.fixedpowerups.FixedPowerups;
import mc.yqt.fixedpowerups.utils.Util;
import mc.yqt.fixedpowerups.utils.RunnableBuilder;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.List;

public abstract class Powerup {

	protected String name;
	protected List<String> lore;
	protected ItemStack icon;
	protected PowerupType type;
	protected boolean canceled;

	protected int lengthInSeconds;
	protected int runtimeDelayInSeconds;
	protected int runtimePeriodInTicks;
	private BukkitTask runtime;
	private boolean reqNMS;

	protected FixedPowerups main;
	protected Player player;

	public Powerup(Player player) {
		this.player = player;
		this.canceled = false;
		this.reqNMS = false;
		this.lengthInSeconds = -1;
		this.runtimeDelayInSeconds = 0;
		this.runtimePeriodInTicks = 0;
		this.lore = null;
	}

	// required powerup methods
	public abstract boolean powerupValidate();
	public abstract void powerupActivate();
	public abstract void powerupShutdown();
	public abstract void playerQuit(Player player);

	public void cancel() {
		this.canceled = true;
		if(this.runtime != null)
			this.runtime.cancel();
		this.runtime = null;
		this.main.setActive(null);
	}

	/**
	 * Validates that the powerup is ready to launch and then launches if true.
	 * Call {@link #powerup(Player)} to force run.
	 * @param player
	 */
	public boolean launch() {
		if (main.getActive() != null) {
			player.sendMessage(ChatColor.RED + "Another powerup is currently active!");
			return false;
		}

		if (reqNMS && !FixedPowerups.getNMSState()) {
			player.sendMessage(ChatColor.RED + "NMS is disabled!");
			return false;
		}

		if (!powerupValidate()) {
			return false;
		}

		// it is safe to launch the powerup now
		powerup();
		return true;
	}

	/**
	 * If this method will not be overwritten, be sure to pass 0 runtime delay
	 * through the constructor
	 */
	public void powerupRuntime() { }

	public void powerup() {
		/*
		 * Main powerup method, split into three parts ACTIVATE: Runs once
		 * activated RUNTIME: Bukkit runnable that runs while the powerup is
		 * active, determined by the delay given in constructor SHUTDOWN: Bukkit
		 * runnable that deactivates the powerup
		 */

		// activate
		Bukkit.broadcastMessage(
				Util.modulate(player.getName() + " has activated the " + Util.encapsulate(this.name) + " powerup!"));
		this.main.setActive(this);
		// give player regen 2 for 30 seconds as standard
		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600, 1), true);

		this.powerupActivate();

		// runtime runnable
		if(this.runtimeDelayInSeconds > 0)
			if(this.runtimePeriodInTicks > 0)
				runtime = RunnableBuilder.make(main)
					.delay(runtimeDelayInSeconds * 20)
					.interval(runtimePeriodInTicks)
					.unlim()
					.run(() -> powerupRuntime());
			else
				runtime = RunnableBuilder.make(main).delay(runtimeDelayInSeconds * 20).run(() -> powerupRuntime());

		// shutdown runnable
		RunnableBuilder.make(main).delay(lengthInSeconds * 20).run(() -> {
			if(canceled)
				return;

			powerupShutdown();

			if(runtime != null) {
				runtime.cancel();
				runtime = null;
			}

			main.setActive(null);

			if(lengthInSeconds > 0)
				Bukkit.broadcastMessage(Util.modulate("The " + Util.encapsulate(name) + " powerup has been disabled."));
		});
	}
	
	/**
	 * Stops the runnables from running and terminates the powerup silently.
	 */
	public void stop() {
		if(runtime != null) {
			runtime.cancel();
			runtime = null;
		}

		main.setActive(null);
		canceled = true;
	}

	public boolean isWrapper() {
		return player == null;
	}

	public String getName() {
		return this.name;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	public void setLore(String... strings) {
		this.lore = Arrays.asList(strings);
	}

	public List<String> getLore() {
		return this.lore;
	}

	public ItemStack getIcon() {
		return icon;
	}

	public PowerupType getType() {
		return type;
	}

	/**
	 * Sets whether this powerup uses NMS code.
	 * @param req
	 */
	public void nms(boolean req) {
		this.reqNMS = req;
	}

	public boolean requiresNMS() {
		return this.reqNMS;
	}

	public boolean isCanceled() {
		return this.canceled;
	}

	public void setMain(FixedPowerups main) {
		this.main = main;
	}
}
