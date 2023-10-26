package bambi.kinematics.events;

import bambi.kinematics.Kinematics;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;
import java.util.List;

public class ExplodeEvent implements Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onExplosionEvent(EntityExplodeEvent event) {
        if (Kinematics.isProtection()) {
            List<Block> blockList = event.blockList();
            for (int i = blockList.size() - 1; i >= 0; --i) {
                if (!Kinematics.getNotProtected().contains(blockList.get(i).getType())) {
                    blockList.remove(i);
                }
            }
        }
        if (event.getEntityType().equals(EntityType.PRIMED_TNT)) {
            Kinematics.add(event);
        }
    }
}

