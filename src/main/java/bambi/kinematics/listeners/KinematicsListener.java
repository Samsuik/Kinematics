package bambi.kinematics.listeners;

import bambi.kinematics.Kinematics;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public final class KinematicsListener implements Listener {
    private final Kinematics kinematics;

    public KinematicsListener(Kinematics kinematics) {
        this.kinematics = kinematics;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onExplosionEvent(EntityExplodeEvent event) {
        if (kinematics.getConfiguration().protection) {
            event.blockList().removeIf(block -> !kinematics.getConfiguration().notProtected.contains(block.getType()));
        }
        if (event.getEntityType().equals(EntityType.PRIMED_TNT)) {
            kinematics.getEventManager().schedule(event);
        }
    }

    @EventHandler
    public void onBlockChange(EntityChangeBlockEvent e) {
        if (e.getEntityType().equals(EntityType.FALLING_BLOCK) && e.getTo().hasGravity()) {
            kinematics.getEventManager().schedule(e);
        }
    }
}
