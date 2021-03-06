package mc.yqt.fixedpowerups.powerups.witherwarrior.withertypes;

import mc.yqt.fixedpowerups.powerups.witherwarrior.HarmlessWitherSkull;
import mc.yqt.fixedpowerups.powerups.witherwarrior.RideableWither;
import org.bukkit.entity.LivingEntity;

public abstract class WitherType {

    private String displayName;
    private int maxHealth;

    public WitherType(String s, int h) {
        this.displayName = s;
        this.maxHealth = h;
    }

    public abstract void attack(HarmlessWitherSkull skull, RideableWither wither, LivingEntity e);

    public abstract void attackCosmetic(HarmlessWitherSkull e);

    public String getDisplayName() {
        return this.displayName;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }
}
