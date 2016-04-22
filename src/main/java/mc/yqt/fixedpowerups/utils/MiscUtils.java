package mc.yqt.fixedpowerups.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;


public class MiscUtils {

	/* Class for just anything random that I need */

    /**
     * Method for getting the surface at a certain x & y coordinate
     *
     * @param Location
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

    public static String modulate(String message) {
        return ChatColor.YELLOW + message;
    }

    public static String encapsulate(String cap) {
        return ChatColor.GREEN + ChatColor.BOLD.toString() + cap + ChatColor.YELLOW;
    }
}
