package mc.yqt.fixedpowerups.powerups.witherwarrior.withertypes;

import com.comphenix.protocol.wrappers.EnumWrappers.Particle;
import mc.yqt.fixedpowerups.powerups.witherwarrior.HarmlessWitherSkull;
import mc.yqt.fixedpowerups.powerups.witherwarrior.RideableWither;
import mc.yqt.fixedpowerups.utils.Particles;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class PeacefulType extends WitherType {

    public PeacefulType() {
        super(ChatColor.GREEN + "PEACEFUL", 300);
    }

    @Override
    public void attack(HarmlessWitherSkull skull, RideableWither wither, LivingEntity e) {
        //don't attack the target, give them regeneration and saturation
        if (skull.isCharged()) {
            e.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 3), true);
            e.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 1), true);
        } else {
            e.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 0));
            e.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 0));
        }
    }

    @Override
    public void attackCosmetic(HarmlessWitherSkull e) {
        //spawn heart particles on ground
        Particles.spawnParticle(Particle.HEART, e.getBukkitEntity().getLocation(), new Vector(3, 0, 3), 15);
    }

}
