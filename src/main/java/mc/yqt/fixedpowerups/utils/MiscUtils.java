package mc.yqt.fixedpowerups.utils;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;


public class MiscUtils {

	/* Class for just anything random that I need */

    /**
     * Method for getting the surface at a certain x & y coordinate
     * @param l location
     * @return New location
     */
    public static Location getSurface(Location l) {

        for (int y = 256; y > 0; y--) {
            Block b = l.getWorld().getBlockAt(l.getBlockX(), y, l.getBlockZ());

            if (b.getType() != Material.AIR && !b.getType().isTransparent())
                return new Location(l.getWorld(), l.getX(), y + 1, l.getZ(), l.getYaw(), l.getPitch());
        }

        return new Location(l.getWorld(), l.getX(), 257, l.getZ(), l.getYaw(), l.getPitch());
    }
    
    /**
     * Method to play a specific sound, volume and pitch to all players online at their locations.
     * @param sound
     * @param volume Float value from 0 to 1.
     * @param pitch Float value from 0 to 1.
     */
    public static void playSound(Sound sound, float volume, float pitch) {
    	playSound(sound, volume, pitch, Bukkit.getOnlinePlayers());
    }
    
    /**
     * Method to play a specific sound, volume and pitch to all given players at their locations.
     * @param sound
     * @param volume Float value from 0 to 1.
     * @param pitch Float value from 0.5 to 2.
     * @param players
     */
    public static void playSound(Sound sound, float volume, float pitch, Collection<? extends Player> players) {
    	for(Player p : players) 
    		p.playSound(p.getLocation(), sound, volume, pitch);
    }

    public static String modulate(String message) {
        return ChatColor.YELLOW + message;
    }

    public static String encapsulate(String cap) {
        return ChatColor.GREEN + ChatColor.BOLD.toString() + cap + ChatColor.YELLOW;
    }
}
