package mc.yqt.fixedpowerups.powerups.witherwarrior;

import java.util.List;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftWither;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

import mc.yqt.fixedpowerups.FixedPowerups;
import mc.yqt.fixedpowerups.listeners.ProtocolListeners;
import mc.yqt.fixedpowerups.utils.NMSReflect;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class RideableWither extends EntityWither {

	private Player pax;
	private int moveDownwards = 0;
	
	public RideableWither(World world) {
		
		super(((CraftWorld) world).getHandle());
		
		//clear existing path finding and target finding
		((List <?>) NMSReflect.getPrivateField("b", PathfinderGoalSelector.class, this.goalSelector)).clear();
		((List <?>) NMSReflect.getPrivateField("c", PathfinderGoalSelector.class, this.goalSelector)).clear();
		((List <?>) NMSReflect.getPrivateField("c", PathfinderGoalSelector.class, this.targetSelector)).clear();
		((List <?>) NMSReflect.getPrivateField("c", PathfinderGoalSelector.class, this.targetSelector)).clear();
		
		//eventually add a pathfinder
	}
	
	@Override
	public void h() {
		super.h();
		
		//set name, delayed slightly otherwise datawatcher is not properly instantiated
		new BukkitRunnable() {
			@Override
			public void run() {
				datawatcher.watch(2, "§e" + pax.getName() + " the §cWITHER WARRIOR");
			}
		}.runTaskLater(FixedPowerups.getThis(), 1L);
	}
	
	@Override
	public void E() {
		//stop wither from damaging blocks
	}
	
	@Override
	public void g(float sideMot, float forMot) {
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
			if(this.moveDownwards > 0) {
				//if shift has been pressed to get wither to descend
				this.motY = -0.5D;
				moveDownwards--;
			} else
				//wither will otherwise naturally descend, so stop that
				this.motY = 0.0D;
		}
		
		//make sure wither follows passenger yaw and pitch
		this.yaw = passenger.yaw;
		this.pitch = passenger.pitch;
		
	    super.g(movStrafe, movForward);
	}
	
	@Override
	public void initAttributes() {
		//override the default movement speed to make wither quicker
		super.initAttributes();
		
	    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(1.0D);
	}
	
	private void descend() {
		//move downwards
		this.moveDownwards++;
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
	
	/* Static methods */
	
	/**
	 * Handles entity dismount event for this mob
	 * @param Event
	 */
	public static void dismountEvent(final EntityDismountEvent e) {
		CraftWither cw = (CraftWither) e.getDismounted();
		
		if(cw.getHandle() instanceof RideableWither) {
			final RideableWither rw = (RideableWither) cw.getHandle();
			
			rw.descend();
			
			//intercept the packet that gets sent when the player dismounts
			ProtocolListeners.interceptWitherDetachPacket = cw.getEntityId();
			
			new BukkitRunnable() {
				@Override
				public void run() {
					rw.setPassenger((Player) e.getEntity());
					ProtocolListeners.interceptWitherDetachPacket = 0;
				}
			}.runTaskLater(FixedPowerups.getThis(), 1L);
		}
	}

}
