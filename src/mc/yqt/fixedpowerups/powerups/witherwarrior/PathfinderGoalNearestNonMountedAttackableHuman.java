package mc.yqt.fixedpowerups.powerups.witherwarrior;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.entity.EntityTargetEvent;

import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.PathfinderGoalTarget;

public class PathfinderGoalNearestNonMountedAttackableHuman extends PathfinderGoalTarget {

	/* 
	 * A wordy class name to describe exactly what is going on here
	 * The RideableWither.class entities use this to target a player that is not their passenger
	 */
	
	private static final int ATTACK_RANGE = 30;
	
	private Player newTarget;
	
	public PathfinderGoalNearestNonMountedAttackableHuman(EntityCreature creature, boolean flag) {
		super(creature, flag);
	}

	@Override
	public boolean a() {
		//using bukkit methods to find nearest player, don't feel like looking through all that obfuscated NMS code to do whatever they do
		Player target = nearestPlayer(this.e.getBukkitEntity().getLocation());
		
		if(target != null) {
			this.newTarget = target;
			this.attackTarget();
			return true;
		} else
			return false;
	}
	
	@Override
	public void c() {
		this.e.setGoalTarget(((CraftPlayer) this.newTarget).getHandle(), EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true);
		
		super.c();
	}
	
	//if valid target, try and attack
	private void attackTarget() {
		if(!this.e.hasLineOfSight(((CraftPlayer) this.newTarget).getHandle()))
			return;
		
		((RideableWither) this.e).a(((CraftPlayer) this.newTarget).getHandle(), 0.0F);
	}
	
	//find nearest player using Bukkit API
	private static Player nearestPlayer(Location witherLoc) {
		Player nearest = null;
		double dist = 100000.0;
		
		
		//some people say using getNearbyEntities() is more efficient, but I really really doubt it considering that getNearbyEntities goes over every entity and checks if the necessary chunks are loaded for the ones in range
		for(Player p : Bukkit.getOnlinePlayers()) {
			double pDist = p.getLocation().distance(witherLoc);
			if(pDist < dist && !(p.getVehicle() instanceof Wither) && p.getGameMode() != GameMode.SPECTATOR) {
				nearest = p;
				dist = pDist;
			}
		}

		if(dist < ATTACK_RANGE)
			return nearest;
		else
			return null;
	}

}
