package mc.yqt.fixedpowerups.powerups.invoker;

import mc.yqt.fixedpowerups.powerups.GeneratePowerup;
import mc.yqt.fixedpowerups.powerups.PowerupType;
import mc.yqt.fixedpowerups.utils.MiscUtils;
import mc.yqt.fixedpowerups.utils.RunnableBuilder;
import mc.yqt.fixedpowerups.utils.Title;
import net.md_5.bungee.api.ChatColor;

import java.util.LinkedList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

@GeneratePowerup
public class DeathScroll extends InvokerScroll {

	private static final int RUNTIME_DELAY = 10; // in seconds
	private static final int RUNTIME_LENGTH = 70; // in seconds
	private static final int RUNTIME_PERIOD = 40; // in ticks
	private static final int LENGTH = RUNTIME_DELAY + RUNTIME_LENGTH;
	
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
	        	if(counter % moduloVal == 0) {
	        		// retrieve a new player using the modulo function
	        		target = activePlayers.get(changed % activePlayers.size());
	        		
	        		// if the target is the activated player, just randomize until there's a unique player
	        		Random r = new Random();
	        		while(target.equals(player))
	        			target = activePlayers.get(r.nextInt(activePlayers.size()));
	        		
	        		// update everyone's title
	        		title.sendSubtitlePacket(activePlayers, ChatColor.RED + target.getName());
	        		MiscUtils.playSound(Sound.NOTE_PLING, 0.5f, 0.8f, activePlayers);
	        		changed++;
	        	}
	        	
	        	counter++;
	        	if(counter % 10 == 0)
	        		moduloVal++;
        	}
        });
        
        // runnable that sets up 
        RunnableBuilder.make(main).delay(RUNTIME_DELAY * 20).run(new Runnable() {
        	// for some reason, reflections is not loading classes with Java 8 code: .run(() -> { });
        	
			@Override
			public void run() {
				Bukkit.broadcastMessage(MiscUtils.modulate("Target is: " + target.getName()));
			}
        });
    }
    
    @Override
    public void powerupRuntime() {
    	//Bukkit.broadcastMessage(MiscUtils.modulate("Target is: " + target.getName()));
    }

    @Override
    public void powerupShutdown() {
        
    }

	@Override
	public boolean powerupValidate() {
		generatePlayerList(player.getWorld());
		
		// powerup can be ran if more than 2 players online
		if(activePlayers.size() > 2)
			return true;
		else {
			player.sendMessage(ChatColor.RED + "Not enough players online!");
			return false;
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
