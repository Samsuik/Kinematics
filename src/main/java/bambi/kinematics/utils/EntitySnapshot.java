package bambi.kinematics.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public final class EntitySnapshot {
    private final Entity entity;
    private final Location location;
    private final Vector velocity;

    public EntitySnapshot(Entity entity) {
        this.entity = entity;
        this.location = entity.getLocation();
        this.velocity = entity.getVelocity();
    }

    public Entity getEntity() {
        return entity;
    }

    public Location getLocation() {
        return location;
    }

    public Vector getVelocity() {
        return velocity;
    }
}
