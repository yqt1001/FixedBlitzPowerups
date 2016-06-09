package mc.yqt.fixedpowerups.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.Particle;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class ProtocolUtil {

	/* Class dedicated to spawning in particles using ProtocolLib */
	private static final ProtocolManager PM = ProtocolLibrary.getProtocolManager();

	/**
	 * Spawn particles
	 * @param Particle type
	 * @param Location
	 * @param Vector offset
	 * @param Number of particles
	 */
	public static void spawnParticle(Particle particle, Location loc, Vector offset, int numOfParticles) {
		spawnParticle(particle, loc, offset, numOfParticles, new int[] {});
	}

	/**
	 * Spawn particles
	 * @param Particle type
	 * @param Location
	 * @param Vector offset
	 * @param Number of particles
	 * @param Particle data
	 */
	public static void spawnParticle(Particle particle, Location loc, Vector offset, int numOfParticles, int data[]) {
		// create packet
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

		// send to all players
		for(Player p : Bukkit.getOnlinePlayers()) {
			try {
				PM.sendServerPacket(p, packet);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sends the message to the player as an action bar message.
	 * @param player
	 * @param msg
	 */
	public static void sendActionBarPacket(Player player, String msg) {
		// create packet
		PacketContainer packet = PM.createPacket(PacketType.Play.Server.CHAT);
		packet.getChatComponents().write(0, WrappedChatComponent.fromText(msg));
		packet.getBytes().write(0, (byte) 2);
		
		// send to player
		try {
			PM.sendServerPacket(player, packet);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends the message to the players as an action bar message.
	 * @param players
	 * @param msg
	 */
	public static void sendActionBarPacket(Collection<Player> players, String msg) {
		// create packet
		PacketContainer packet = PM.createPacket(PacketType.Play.Server.CHAT);
		packet.getChatComponents().write(0, WrappedChatComponent.fromText(msg));
		packet.getBytes().write(0, (byte) 2);
		
		// send to players
		for(Player player : players)
			try {
				PM.sendServerPacket(player, packet);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
	}
}
