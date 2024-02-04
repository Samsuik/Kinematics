package bambi.kinematics.listeners;

import bambi.kinematics.Kinematics;
import bambi.kinematics.enums.AlertType;
import bambi.kinematics.player.KinematicsPlayer;
import bambi.kinematics.utils.TemplatedMessage;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.List;
import java.util.Locale;

public final class EntitySpawnListener implements Listener, Runnable {
    private static final boolean MODERN = !Bukkit.getServer().getBukkitVersion().contains("1.8");
    private int tick = 0;
    private final Kinematics kinematics;

    public EntitySpawnListener(Kinematics kinematics) {
        this.kinematics = kinematics;
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (MODERN) {
            Entity entity = event.getEntity();

            if (entity.getType() == EntityType.PRIMED_TNT || entity.getType() == EntityType.FALLING_BLOCK) {
                notifyThatAnEntitySpawned(entity);
            }
        }
    }

    @Override
    public void run() {
        if (!MODERN) {
            for (World w : Bukkit.getServer().getWorlds()) {
                // this is collected from the world entity list, so it should be naturally sorted (oldest -> newest)
                List<Entity> entities = w.getEntities();
                for (int i = entities.size() - 1; i >= 0; i--) {
                    Entity entity = entities.get(i);
                    if (entity.getTicksLived() > 1)
                        break;
                    if (entity.getTicksLived() == 0 || entity.getType() != EntityType.PRIMED_TNT && entity.getType() != EntityType.FALLING_BLOCK)
                        continue;
                    notifyThatAnEntitySpawned(entity);
                }
            }
        }

        tick++;
    }

    private void notifyThatAnEntitySpawned(Entity entity) {
        List<KinematicsPlayer> nearbyPlayers = kinematics.getPlayerManager().nearbyPlayers(entity.getLocation(),
                kplayer -> kplayer.isViewingAlert(AlertType.ENTITY_SPAWN));

        nearbyPlayers.forEach(kplayer -> kplayer.sendTemplatedMessage(new TemplatedMessage(
                kinematics.getConfiguration().entityspawntemplate, entity.getLocation().toVector(),
                entity.getVelocity(), tick, -1, -1, entity.getName().toLowerCase(Locale.ROOT),
                entity.getType() == EntityType.PRIMED_TNT ? NamedTextColor.RED : NamedTextColor.YELLOW
        )));
    }

}
