package mc.yqt.fixedpowerups.powerups.invoker;

import mc.yqt.fixedpowerups.powerups.GeneratePowerup;
import mc.yqt.fixedpowerups.powerups.PowerupType;
import mc.yqt.fixedpowerups.utils.Util;
import mc.yqt.fixedpowerups.utils.ProtocolUtil;
import mc.yqt.fixedpowerups.utils.RunnableBuilder;
import mc.yqt.fixedpowerups.utils.Title;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

@GeneratePowerup
public class DeathScroll extends InvokerScroll {

	private static final int RUNTIME_DELAY = 10; // in seconds
	private static final int RUNTIME_LENGTH = 70; // in seconds
	private static final int RUNTIME_PERIOD = 40; // in ticks
	private static final int LENGTH = RUNTIME_DELAY + RUNTIME_LENGTH;

	private static final int DISTANCE_NEEDED = 30;

	private Player target;
	private Title title;

	private LinkedList<Player> activePlayers = new LinkedList<>();
	private int counter = 0;
	private int changed = 0;
	private int moduloVal = 2;

	public DeathScroll(Player player) {
		super(player);

		name = "Death Curse";
		type = PowerupType.INVOKER;
		lengthInSeconds = LENGTH;
		runtimeDelayInSeconds = RUNTIME_DELAY;
		runtimePeriodInTicks = RUNTIME_PERIOD;
	}

	@Override
	public void powerupActivate() {
		// create randomizing title
		title = new Title("", ChatColor.RED + activePlayers.get(0).getName(), RUNTIME_DELAY * 20 + 20, 0, 5);
		title.sendCreationPackets(activePlayers);

		// runnable that randomizes which player will be selected
		RunnableBuilder.make(main).limit(RUNTIME_DELAY * 20).run(new Runnable() {
			// for some reason, reflections is not loading classes with Java 8 code: .run(() -> { });

			@Override
			public void run() {
				if(canceled)
					return;
				
				if(counter % moduloVal == 0) {
					// retrieve a new player using the modulo function
					target = activePlayers.get(changed % activePlayers.size());

					// if the target is the activated player, just randomize
					// until there's a unique player
					while(target.equals(player))
						target = Util.randElement(activePlayers);

					// update everyone's title
					title.sendSubtitlePacket(activePlayers, ChatColor.RED + target.getName());
					Util.playSound(Sound.NOTE_PLING, 0.5f, 0.8f, activePlayers);
					changed++;
				}

				counter++;
				if(counter % 10 == 0)
					moduloVal++;
			}
		});

		// runnable that uses titles to show who the target is
		RunnableBuilder.make(main).delay(RUNTIME_DELAY * 20).run(new Runnable() {
			// for some reason, reflections is not loading classes with Java 8 code: .run(() -> { });

			@Override
			public void run() {
				if(canceled)
					return;
				
				title.sendTitlePacket(activePlayers, "" + ChatColor.RED + ChatColor.BOLD + "TARGET IS");
				title.sendSubtitlePacket(activePlayers, ChatColor.GOLD + target.getName());
			}
		});
	}

	@Override
	public void powerupRuntime() {
		trailGen();

		// broadcast distance between the two using the action bar
		double distance = player.getLocation().distance(target.getLocation());
		distance = Math.round(distance * 100.0D) / 100.0D;

		String msg = ChatColor.AQUA + "" + distance + "m";
		ProtocolUtil.sendActionBarPacket(player, msg);
		ProtocolUtil.sendActionBarPacket(target, msg);
	}

	@Override
	public void powerupShutdown() {
		// hunter wasn't able to kill or get close enough to target
		if(player.getLocation().distance(target.getLocation()) > DISTANCE_NEEDED) {
			Bukkit.broadcastMessage(Util.modulate(
					Util.encapsulate(player.getName()) + " was not able to get close enough to the target in time!"));

			Title.createTitle(ChatColor.GREEN + "YOU ARE SAFE!", "", 20, 10, 10, target);
			Title.createTitle(ChatColor.RED + "You failed.", "", 20, 10, 10, player);

			return;
		}

		killTarget();
		
		Bukkit.broadcastMessage(Util.modulate(Util.encapsulate(player.getName())) + " was able to get within 30 blocks to kill the target!");
	}

	@Override
	public boolean powerupValidate() {
		generatePlayerList(player.getWorld());

		// powerup can be ran if more than 2 players online
		if (activePlayers.size() > 2)
			return true;
		else {
			player.sendMessage(ChatColor.RED + "Not enough players online!");
			return false;
		}
	}
	
	@Override
	public void playerQuit(Player player) {
		// if the target or hunter quits
		if(!player.equals(this.player) && !player.equals(target))
			return;
		
		Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " quit! Death Scroll powerup canceled.");
		stop();
	}

	public void onDamage(EntityDamageByEntityEvent e) {
		// must be player hitting target
		if(!e.getEntity().equals(target) || !e.getDamager().equals(player))
			return;

		killTarget();

		// turn off the powerup
		stop();
		Bukkit.broadcastMessage(Util.modulate("The Death Scroll target has died!"));
	}
	
	private void killTarget() {
		// instantly kill the target
		target.damage(20.0f, player);

		// a lot of lightning around the kill site
		for(int i = 0; i < 10; i++)
			player.getWorld().strikeLightningEffect(Util.randomize(target.getLocation()));
	}

	private void trailGen() {
		// generate trail of particles for both players indicating where to go

		double length = player.getLocation().distance(target.getLocation());
		// don't generate trail if too close
		if(length < 8)
			return;

		// if the trail is too long, shorten the generated trail
		if(length > 50)
			length = 50;

		// start with trail from player
		Location l = player.getLocation().clone();
		l.add(0, 1, 0);
		Vector toTarget = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
		for(int i = 0; i < length; i++) {
			// skip first couple places of the trail to prevent annoying
			// particles
			if(i > 2) {
				player.spigot().playEffect(Util.randomizeSlightly(l), Effect.COLOURED_DUST, 0, 1, 1f, 0f, 0f, 1, 0,
						100);
				player.spigot().playEffect(Util.randomizeSlightly(l), Effect.COLOURED_DUST, 0, 1, 1f, 0f, 0f, 1, 0,
						100);
			}

			// add unit vector to location
			l.add(toTarget);
		}

		// now trail from target
		l = target.getLocation().clone();
		l.add(0, 1, 0);
		Vector toPlayer = player.getLocation().toVector().subtract(target.getLocation().toVector()).normalize();
		for(int i = 0; i < length; i++) {
			// skip first couple places of the trail to prevent annoying
			// particles
			if(i > 2) {
				target.spigot().playEffect(Util.randomizeSlightly(l), Effect.COLOURED_DUST, 0, 1, 1f, 0f, 0f, 1, 0,
						100);
				target.spigot().playEffect(Util.randomizeSlightly(l), Effect.COLOURED_DUST, 0, 1, 1f, 0f, 0f, 1, 0,
						100);
			}

			// add unit vector to location
			l.add(toPlayer);
		}
	}

	private void generatePlayerList(World w) {
		activePlayers.clear();

		// adds all players in the world to the list
		for(Player p : Bukkit.getOnlinePlayers())
			if(p.getWorld().equals(w))
				activePlayers.add(p);
	}
}
