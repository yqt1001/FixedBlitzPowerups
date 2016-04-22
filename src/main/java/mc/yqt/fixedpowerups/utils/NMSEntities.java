package mc.yqt.fixedpowerups.utils;

import mc.yqt.fixedpowerups.powerups.witherwarrior.HarmlessWitherSkull;
import mc.yqt.fixedpowerups.powerups.witherwarrior.RideableWither;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityTypes;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.util.Map;

public enum NMSEntities {

    RIDEABLE_WITHER("Wither", 64, RideableWither.class),
    HARMLESS_SKULL("WitherSkull", 19, HarmlessWitherSkull.class);

    @SuppressWarnings("unchecked")
    private NMSEntities(String name, int id, Class<? extends Entity> e) {
        ((Map<String, Class<? extends Entity>>) NMSReflect.getPrivateField("c", EntityTypes.class, null)).put(name, e);
        ((Map<Class<? extends Entity>, String>) NMSReflect.getPrivateField("d", EntityTypes.class, null)).put(e, name);
        ((Map<Class<? extends Entity>, Integer>) NMSReflect.getPrivateField("f", EntityTypes.class, null)).put(e, Integer.valueOf(id));
    }

    public static Entity spawnEntity(Entity e, Location l) {
        e.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
        ((CraftWorld) l.getWorld()).getHandle().addEntity(e);

        return e;
    }
}
