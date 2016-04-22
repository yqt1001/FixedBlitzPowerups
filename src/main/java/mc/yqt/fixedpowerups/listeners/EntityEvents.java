package mc.yqt.fixedpowerups.listeners;

import mc.yqt.fixedpowerups.FixedPowerups;
import mc.yqt.fixedpowerups.powerups.invoker.Invoker;
import mc.yqt.fixedpowerups.powerups.invoker.InvokerScroll;
import mc.yqt.fixedpowerups.powerups.witherwarrior.WitherWarrior;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;

public class EntityEvents implements Listener {

    private FixedPowerups main;

    public EntityEvents(FixedPowerups main) {
        this.main = main;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!e.hasItem()) return;
        //invoker scroll interact
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR) || !(e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (e.getItem().getType() != Material.PAPER) return;
        if (e.getItem().getItemMeta() == null || e.getItem().getItemMeta().getDisplayName() == null) return;

        String s = e.getItem().getItemMeta().getDisplayName().substring(2);
        if (!Invoker.getScrolls().containsKey(s)) return;
        InvokerScroll is = Invoker.getScrolls().get(s);

        //check NMS is enabled, if scroll requires NMS
        if (is.requiresNMS() && !FixedPowerups.getNMSState()) {
            e.getPlayer().sendMessage(ChatColor.RED + "NMS is disabled!");
            return;
        }

        //activate scroll powerrup
        is.powerup(e.getPlayer());

        //remove all scrolls from inventory
        for (ItemStack i : e.getPlayer().getInventory().getContents())
            if (i.getType() == Material.PAPER)
                if (i.getItemMeta() != null && i.getItemMeta().getDisplayName() != null) {
                    s = i.getItemMeta().getDisplayName().substring(2);
                    if (Invoker.getScrolls().containsKey(s))
                        i.setAmount(0);
                }


    }

    @EventHandler
    public void onDismount(VehicleExitEvent e) {
        WitherWarrior active;
        if (e.getExited() instanceof Wither && (active = this.main.getActive(WitherWarrior.class)) != null)
            active.getWither().dismountEvent(e);
    }
}
