package mc.yqt.fixedpowerups.powerups.witherwarrior;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.entity.EntityTargetEvent;

import mc.yqt.fixedpowerups.FixedPowerups;
import mc.yqt.fixedpowerups.utils.NMSReflect;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.PathfinderGoalTarget;

public class PathfinderGoalNearestNonMountedAttackableHuman extends PathfinderGoalTarget {

	/* 
	 * A wordy class name to describe exactly what is going on here
	 * The RideableWither.class entities use this to target a player that is not their passenger
	 */
	
	private Player newTarget;
	
	public PathfinderGoalNearestNonMountedAttackableHuman(EntityCreature creature, boolean flag) {
		super(creature, flag);
	}

	@Override
	public boolean a() {
		//using bukkit methods to find nearest player, don't feel like looking through all that obfuscated NMS code to do whatever they do
		Player target = nearestPlayer(new Location((World) this.e.getWorld().getWorld(), this.e.locX, this.e.locY, this.e.locZ));
		
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
		
		try {
			Method attack = NMSReflect.getPrivateMethod("a", EntityWither.class, int.class, EntityLiving.class);
			//attack.invoke(this.e, 0, ((CraftPlayer) this.newTarget).getHandle());
		} catch (Exception e) {
			FixedPowerups.setNMSState(false);
			e.printStackTrace();
		}
	}
	
	//find nearest player using Bukkit API
	private static Player nearestPlayer(Location witherLoc) {
		Player nearest = null;
		double dist = 100000.0;
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.getLocation().distance(witherLoc) < dist && !(p.getVehicle() instanceof Wither)) {
				nearest = p;
				dist = p.getLocation().distance(witherLoc);
			}
		}
		
		if(dist < 10) 
			return nearest;
		 else
			return null;
	}

}
