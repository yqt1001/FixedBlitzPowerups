package mc.yqt.fixedpowerups.powerups.witherwarrior.withertypes;

import org.bukkit.entity.LivingEntity;

import mc.yqt.fixedpowerups.powerups.witherwarrior.HarmlessWitherSkull;
import mc.yqt.fixedpowerups.powerups.witherwarrior.RideableWither;

public abstract class WitherType {

	private String displayName;
	private int maxHealth;
	
	public abstract void attack(HarmlessWitherSkull skull, RideableWither wither, LivingEntity e);
	public abstract void attackCosmetic(HarmlessWitherSkull e);
	
	public WitherType(String s, int h) {
		this.displayName = s;
		this.maxHealth = h;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	public int getMaxHealth() {
		return this.maxHealth;
	}
}
