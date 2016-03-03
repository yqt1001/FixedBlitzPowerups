package mc.yqt.fixedpowerups.powerups.witherwarrior;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftWither;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

import mc.yqt.fixedpowerups.FixedPowerups;
import mc.yqt.fixedpowerups.powerups.witherwarrior.withertypes.AngryType;
import mc.yqt.fixedpowerups.powerups.witherwarrior.withertypes.LethalType;
import mc.yqt.fixedpowerups.powerups.witherwarrior.withertypes.PeacefulType;
import mc.yqt.fixedpowerups.powerups.witherwarrior.withertypes.WitherType;
import mc.yqt.fixedpowerups.utils.NMSEntities;
import mc.yqt.fixedpowerups.utils.NMSReflect;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class RideableWither extends EntityWither {
	
	/* Wither types enum */
	public enum WitherTypes {
		PEACEFUL(new PeacefulType()), LETHAL(new LethalType()), ANGRY(new AngryType());
		
		private WitherType obj;
		
		private WitherTypes(WitherType wt) {
			this.obj = wt;
		}
		
		public WitherType getType() {
			return this.obj;
		}
	}
	
	private FixedPowerups main;
	private Player pax;
	private WitherTypes type;

	public RideableWither(FixedPowerups main, World world, WitherTypes type) {
		super(((CraftWorld) world).getHandle());
		
		this.type = type;
		this.main = main;

		//clear existing path finding and target selectors
		((List<?>) NMSReflect.getPrivateField("b", PathfinderGoalSelector.class, this.goalSelector)).clear();
		((List<?>) NMSReflect.getPrivateField("c", PathfinderGoalSelector.class, this.goalSelector)).clear();
		((List<?>) NMSReflect.getPrivateField("b", PathfinderGoalSelector.class, this.targetSelector)).clear();
		((List<?>) NMSReflect.getPrivateField("c", PathfinderGoalSelector.class, this.targetSelector)).clear();

		//add a target, nearest player
		this.targetSelector.a(1, new PathfinderGoalNearestNonMountedAttackableHuman(this, false));
		
		//set name, delayed slightly otherwise datawatcher is not properly instantiated
		new BukkitRunnable() {
			@Override
			public void run() {
				datawatcher.watch(2, "§e" + pax.getName() + " the §cWITHER WARRIOR");
			}
		}.runTaskLater(this.main, 1L);
	}

	@Override
	public void h() {
		super.h();

		/*//set name, delayed slightly otherwise datawatcher is not properly instantiated
		new BukkitRunnable() {
			@Override
			public void run() {
				datawatcher.watch(2, "§e" + pax.getName() + " the §cWITHER WARRIOR");
			}
		}.runTaskLater(this.main, 1L);*/
	}

	@Override
	public void E() {
		//stop wither from damaging blocks
	}

	@Override
	public void g(float f, float f1) {
		//wither movement

		if((this.passenger == null) || (!(this.passenger instanceof EntityHuman)))
			return;

		EntityLiving passenger = (EntityLiving) this.passenger;

		this.fallDistance = 0.0F;

		//get passenger keyboard inputs
		float movForward = passenger.ba;
		float movStrafe = passenger.aZ;
		boolean jump = (boolean) NMSReflect.getPrivateField("aY", EntityLiving.class, passenger);

		//move upwards
		if(jump)
			this.motY = 0.3D;
		else {
			if(movForward == 0)
				//if S and W or nothing has been pressed, descend slowly
				this.motY = -0.1D;
			else if(movForward < 0) {
				//if S has been pressed, descend
				this.motY = -0.2D;
				movForward = 0;
			} else
				//wither will otherwise naturally descend, so stop that
				this.motY = 0.0D;
		}

		//make sure wither follows passenger yaw and pitch
		this.yaw = passenger.yaw;
		this.pitch = passenger.pitch;

		//I still want to try and speed up the wither but is it that important? I'm not sure

		super.g(movStrafe, movForward);

		//force update the target, that way it's not incredibly unfair for a single player
		try {
			Object targeter = ((List<?>) NMSReflect.getPrivateField("b", PathfinderGoalSelector.class, this.targetSelector)).get(0);
			Class<?> pfitemclass = PathfinderGoalSelector.class.getDeclaredClasses()[0];
			PathfinderGoal pfg = (PathfinderGoal) NMSReflect.getPrivateField("a", pfitemclass, targeter);

			if(pfg.a())
				pfg.c();
		} catch(Exception e) {
			FixedPowerups.setNMSState(false);
			e.printStackTrace();
		}
	}

	@Override
	public void a(EntityLiving entityliving, float f) {
		//custom wither skull

		//spawn about twice per second
		if(Math.random() > 0.12)
			return;

		this.world.a(null, 1014, new BlockPosition(this), 0);

		double d3 = this.locX;
		double d4 = this.locY + 3.0D;
		double d5 = this.locZ;
		double d6 = entityliving.locX - d3;
		double d7 = (entityliving.locY + entityliving.getHeadHeight() * 0.5D) - d4;
		double d8 = entityliving.locZ - d5;

		NMSEntities.spawnEntity(
				new HarmlessWitherSkull(this.pax.getWorld(), this, d6, d7, d8, 
				((CraftPlayer) this.pax).getHandle()), new Location(this.pax.getWorld(), d3, d4, d5));
	}

	/**
	 * Sets the passenger
	 * @param Player
	 */
	public void setPassenger(Player p) {
		((CraftPlayer) p).getHandle().mount(this);
		this.pax = p;
	}

	/**
	 * Gets the passenger
	 * @return
	 */
	public Player getPassenger() {
		return this.pax;
	}
	
	/**
	 * Gets the wither type
	 * @return
	 */
	public WitherTypes getType() {
		return this.type;
	}

	/**
	 * Handles entity dismount event for this mob
	 * @param Event
	 */
	public void dismountEvent(final EntityDismountEvent e) {
		CraftWither cw = (CraftWither) e.getDismounted();

		if(cw.getHandle() instanceof RideableWither) {
			final RideableWither rw = (RideableWither) cw.getHandle();
			new BukkitRunnable() {
				@Override
				public void run() {
					rw.setPassenger((Player) e.getEntity());
				}
			}.runTaskLater(this.main, 1L);
		}
	}
}
