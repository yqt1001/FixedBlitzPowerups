package mc.yqt.fixedpowerups.powerups.witherwarrior;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;

import mc.yqt.fixedpowerups.powerups.witherwarrior.RideableWither.WitherTypes;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityWitherSkull;
import net.minecraft.server.v1_8_R3.MovingObjectPosition;

public class HarmlessWitherSkull extends EntityWitherSkull {

	private EntityPlayer pax;
	private RideableWither wither;
	
	public HarmlessWitherSkull(World world, EntityLiving entityliving, double d0, double d1, double d2, EntityPlayer passenger) {
		super(((CraftWorld) world).getHandle(), entityliving, d0, d1, d2);
		
		this.pax = passenger;
		this.wither = (RideableWither) entityliving;
		
		//1/10 chance of a charged skull
		if(Math.random() < 0.1)
			this.setCharged(true);
	}
	
	@Override
	public void a(MovingObjectPosition movingobjectposition) {
		//taken from Mojang source
		if(this.world.isClientSide)
			return;
		
		Entity e = movingobjectposition.entity;
		WitherTypes type = this.wither.getType();
		
		if(e != null && e instanceof EntityLiving)
		{
			//make sure the passenger is not being hit
			if(e.equals(this.pax))
				return;
			
			//do attack
			type.getType().attack(this, this.wither, (LivingEntity) e.getBukkitEntity());
		}
		
		//do cosmetics
		type.getType().attackCosmetic(this);
		
		
		die();
	}
}
