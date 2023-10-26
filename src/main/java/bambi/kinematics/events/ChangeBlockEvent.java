package bambi.kinematics.events;

import bambi.kinematics.Kinematics;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class ChangeBlockEvent implements Listener {
    @EventHandler
    public void onBlockChange(EntityChangeBlockEvent e) {
        if (e.getEntityType().equals(EntityType.FALLING_BLOCK) && e.getTo().hasGravity()) {
            Kinematics.add(e);
        }
    }
}

