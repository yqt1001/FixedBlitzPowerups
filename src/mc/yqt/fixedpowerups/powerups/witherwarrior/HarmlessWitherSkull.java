package mc.yqt.fixedpowerups.powerups.witherwarrior;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityWitherSkull;
import net.minecraft.server.v1_8_R3.MovingObjectPosition;

public class HarmlessWitherSkull extends EntityWitherSkull {

	private EntityPlayer pax;
	
	public HarmlessWitherSkull(World world, EntityLiving entityliving, double d0, double d1, double d2, EntityPlayer passenger) {
		super(((CraftWorld) world).getHandle(), entityliving, d0, d1, d2);
		
		this.pax = passenger;
	}
	
	@Override
	public void a(MovingObjectPosition movingobjectposition) {
		//for the most part directly copied from Mojang source (at least right now)
		/*if(!this.world.isClientSide)
	    {
			if(movingobjectposition.entity != null)
			{
				boolean didDamage = false;
				if(this.shooter != null)
				{
					didDamage = movingobjectposition.entity.damageEntity(DamageSource.projectile(this, this.shooter), 8.0F);
					if (didDamage) {
						if (!movingobjectposition.entity.isAlive()) {
							this.shooter.heal(5.0F, EntityRegainHealthEvent.RegainReason.WITHER);
						} else {
							a(this.shooter, movingobjectposition.entity);
						}
					}
				}
				else
				{
					didDamage = movingobjectposition.entity.damageEntity(DamageSource.MAGIC, 5.0F);
				}
				if ((didDamage) && ((movingobjectposition.entity instanceof EntityLiving)))
				{
					byte b0 = 0;
					if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
						b0 = 10;
					} else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
					b0 = 40;
					}
					if (b0 > 0) {
						((EntityLiving)movingobjectposition.entity).addEffect(new MobEffect(MobEffectList.WITHER.id, 20 * b0, 1));
					}
				}
			}
			ExplosionPrimeEvent event = new ExplosionPrimeEvent(getBukkitEntity(), 1.0F, false);
			this.world.getServer().getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				this.world.createExplosion(this, this.locX, this.locY, this.locZ, event.getRadius(), event.getFire(), this.world.getGameRules().getBoolean("mobGriefing"));
			}
			die();
	    }*/
		
		//not a fan of the way Mojang handles mobs and damage, so I'm going to use the Bukkit API for the most part
		
		//taken from Mojang source
		if(this.world.isClientSide)
			return;
		
		//boolean didDamage = false;
		Entity e = movingobjectposition.entity;
		
		if(e != null)
		{

			//make sure it's not hitting the wither rider
			if(movingobjectposition.entity.equals(this.pax))
				return;
			
			e.damageEntity(DamageSource.projectile(this, this.shooter), 0.1F);
			
		}
	}
}
