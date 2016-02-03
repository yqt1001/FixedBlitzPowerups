package mc.yqt.fixedpowerups.powerups.witherwarrior.withertypes;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import mc.yqt.fixedpowerups.powerups.witherwarrior.HarmlessWitherSkull;
import mc.yqt.fixedpowerups.powerups.witherwarrior.RideableWither;

public class LethalType extends WitherType {

	public LethalType() {
		super("§8DEADLY", 25);
	}

	@Override
	public void attack(HarmlessWitherSkull skull, RideableWither wither, LivingEntity e) {
		//attack the target as if normal, slightly less damage though
		if(skull.isCharged())
		{
			e.damage(12.0, (Entity) wither.getBukkitEntity());
			e.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 80, 0));
		}
		else
		{
			e.damage(8.0, (Entity) wither.getBukkitEntity());
			e.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 80, 0));
		}
	}

	@Override
	public void attackCosmetic(HarmlessWitherSkull e) {
		//create non-destructive explosion
		Location l = e.getBukkitEntity().getLocation();
		e.getWorld().getWorld().createExplosion(l.getX(), l.getY(), l.getZ(), 1.0F, false, false);
	}

}
