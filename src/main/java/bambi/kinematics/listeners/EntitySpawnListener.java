package bambi.kinematics.listeners;

import bambi.kinematics.Kinematics;
import bambi.kinematics.enums.AlertType;
import bambi.kinematics.player.KinematicsPlayer;
import bambi.kinematics.utils.EntitySnapshot;
import bambi.kinematics.utils.TemplatedMessage;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.*;

public final class EntitySpawnListener implements Listener, Runnable {
    private static boolean MODERN_EVENT_HANDLING = false;

    private final Map<Integer, List<EntitySnapshot>> spawnedEntities = new HashMap<>();
    private int tick = 0;
    private final Kinematics kinematics;

    public EntitySpawnListener(Kinematics kinematics) {
        this.kinematics = kinematics;
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity.getType() == EntityType.PRIMED_TNT || entity.getType() == EntityType.FALLING_BLOCK) {
            queueNotifyEntitySpawn(entity);
            MODERN_EVENT_HANDLING = true;
        }
    }

    @Override
    public void run() {
        if (!MODERN_EVENT_HANDLING) {
            trackNewlySpawnedEntities();
        }

        notifyPlayersOfEntitySpawns();
        removeExpiredEntities();
        tick++;
    }

    private void trackNewlySpawnedEntities() {
        for (World w : Bukkit.getServer().getWorlds()) {
            // this is collected from the world entity list, so it should be naturally sorted (oldest -> newest)
            List<Entity> entities = w.getEntities();
            for (int i = entities.size() - 1; i >= 0; i--) {
                Entity entity = entities.get(i);
                if (entity.getTicksLived() > 1)
                    break;
                if (entity.getTicksLived() == 0 || entity.getType() != EntityType.PRIMED_TNT && entity.getType() != EntityType.FALLING_BLOCK)
                    continue;
                queueNotifyEntitySpawn(entity);
            }
        }
    }

    private void queueNotifyEntitySpawn(Entity entity) {
        spawnedEntities.computeIfAbsent(tick, t -> new ArrayList<>())
                .add(new EntitySnapshot(entity));
    }

    private void removeExpiredEntities() {
        int ttl = kinematics.getPlayerManager().players().stream()
                .mapToInt(kplayer -> kplayer.getProperties().getTicksLived())
                .max()
                .orElse(0);

        spawnedEntities.keySet().removeIf(spawnedTick -> tick - spawnedTick > ttl);
    }

    private void notifyPlayersOfEntitySpawns() {
        spawnedEntities.forEach((spawnedTick, entities) -> {
            for (KinematicsPlayer kplayer : kinematics.getPlayerManager().players()) {
                if (kplayer.isViewingAlert(AlertType.ENTITY_SPAWN) && kplayer.getProperties().getTicksLived() == tick - spawnedTick) {
                    alertPlayerOfNearbyEntities(kplayer, entities);
                }
            }
        });
    }

    private void alertPlayerOfNearbyEntities(KinematicsPlayer kplayer, List<EntitySnapshot> entities) {
        List<EntitySnapshot> alertEntities = entities.stream()
                .filter(entity -> !entity.getEntity().isDead())
                .filter(entity -> kplayer.isInAlertDistance(entity.getLocation()))
                .toList();

        for (EntitySnapshot snapshot : alertEntities) {
            Entity entity = snapshot.getEntity();
            kplayer.sendTemplatedMessage(new TemplatedMessage(
                    kinematics.getConfiguration().entityspawntemplate, snapshot.getLocation().toVector(),
                    snapshot.getVelocity(), tick, -1, -1, entity.getName().toLowerCase(Locale.ENGLISH),
                    entity.getType() == EntityType.PRIMED_TNT ? NamedTextColor.RED : NamedTextColor.YELLOW
            ));
        }
    }
}
