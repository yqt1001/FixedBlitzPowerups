package mc.yqt.fixedpowerups.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.Particle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;

public class Particles {

	/* Class dedicated to spawning in particles using ProtocolLib */

    /**
     * Spawn particles
     *
     * @param Particle
     *         type
     * @param Location
     * @param Vector
     *         offset
     * @param Number
     *         of particles
     */
    public static void spawnParticle(Particle particle, Location loc, Vector offset, int numOfParticles) {
        spawnParticle(particle, loc, offset, numOfParticles, new int[]{});
    }

    /**
     * Spawn particles
     *
     * @param Particle
     *         type
     * @param Location
     * @param Vector
     *         offset
     * @param Number
     *         of particles
     * @param Particle
     *         data
     */
    public static void spawnParticle(Particle particle, Location loc, Vector offset, int numOfParticles, int data[]) {
        ProtocolManager PM = ProtocolLibrary.getProtocolManager();

        //create packet
        PacketContainer packet = PM.createPacket(PacketType.Play.Server.WORLD_PARTICLES);

        packet.getParticles().write(0, particle);
        packet.getFloat().write(0, (float) loc.getX());
        packet.getFloat().write(1, (float) loc.getY());
        packet.getFloat().write(2, (float) loc.getZ());
        packet.getFloat().write(3, (float) offset.getX());
        packet.getFloat().write(4, (float) offset.getY());
        packet.getFloat().write(5, (float) offset.getZ());
        packet.getIntegers().write(0, numOfParticles);
        packet.getIntegerArrays().write(0, data);

        //send to all players
        for (Player p : Bukkit.getOnlinePlayers()) {
            try {
                PM.sendServerPacket(p, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
