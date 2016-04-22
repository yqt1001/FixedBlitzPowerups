package mc.yqt.fixedpowerups.powerups.witherwarrior.withertypes;

import com.comphenix.protocol.wrappers.EnumWrappers.Particle;
import mc.yqt.fixedpowerups.powerups.witherwarrior.HarmlessWitherSkull;
import mc.yqt.fixedpowerups.powerups.witherwarrior.RideableWither;
import mc.yqt.fixedpowerups.utils.Particles;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class AngryType extends WitherType {

    public AngryType() {
        super(ChatColor.RED + "ANGRY", 100);
    }

    @Override
    public void attack(HarmlessWitherSkull skull, RideableWither wither, LivingEntity e) {
        //attack the target for almost no damage, then make them go flying roughly horizontally
        e.damage(0.1, (Entity) wither.getBukkitEntity());

        Vector v = skull.getBukkitEntity().getVelocity();

        if (skull.isCharged()) {
            v.multiply(8);
            v.setY(2.0);
            e.setVelocity(v);
        } else {
            v.multiply(2);
            v.setY(1.0);
            e.setVelocity(v);
        }
    }

    @Override
    public void attackCosmetic(HarmlessWitherSkull e) {
        //spawn coal block breaking particles on ground
        Particles.spawnParticle(Particle.BLOCK_CRACK, e.getBukkitEntity().getLocation(), new Vector(2, 0, 2), 10, new int[]{173});
    }

}
