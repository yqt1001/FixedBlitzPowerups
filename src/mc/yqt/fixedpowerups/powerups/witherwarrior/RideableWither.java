package mc.yqt.fixedpowerups.powerups.witherwarrior;

import java.util.List;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import mc.yqt.fixedpowerups.utils.NMSReflect;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class RideableWither extends EntityWither {

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
	protected void E() {
		//stop wither from damaging blocks
	}
	
	@Override
	public void e(float sideMot, float forMot) {
		if((this.passenger == null) || (!(this.passenger instanceof EntityHuman)))
			return;

	    //EntityHuman human = (EntityHuman)this.passenger;

	    this.lastYaw = (this.yaw = this.passenger.yaw);
	    this.pitch = (this.passenger.pitch * 0.5F);
	    
	    //b(this.yaw, this.pitch);
	    //this.aO = (this.aM = this.yaw);
	    
	    this.aw = 1.0F;
	    
	    float bd = (float) NMSReflect.getPrivateField("bd", EntityLiving.class, this.passenger);
	    float be = (float) NMSReflect.getPrivateField("be", EntityLiving.class, this.passenger);
	    
	    sideMot = (bd * 0.5F);
	    forMot = be;
	    
	    if (forMot <= 0.0F) {
	      forMot *= 0.25F;
	    }
	    sideMot *= 0.75F;
	    
	    //float speed = 0.35F;
	    
	    //i(speed);
	    super.e(sideMot, forMot);
	}

}
