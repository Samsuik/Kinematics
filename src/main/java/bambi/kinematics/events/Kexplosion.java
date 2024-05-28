package bambi.kinematics.events;

import bambi.kinematics.enums.AlertType;
import bambi.kinematics.player.KinematicsPlayer;
import bambi.kinematics.utils.TemplatedMessage;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import java.util.Set;

public final class Kexplosion extends Kevent<EntityExplodeEvent> {
    private final Vector pos;
    private final Vector feet;
    private boolean onGround;
    private Set<Vector> nearby = Set.of();
    private final Vector vel;
    private final Material material;
    private boolean swinging = false;

    public Kexplosion(KinematicsPlayer kplayer, EntityExplodeEvent e, int tick2, int order) {
        super(kplayer, e, tick2, order);
        this.pos = e.getLocation().toVector();
        this.feet = e.getEntity().getLocation().toVector();
        this.onGround = e.getEntity().isOnGround();
        this.vel = e.getEntity().getVelocity();
        this.material = e.getLocation().getBlock().getType();
    }

    @Override
    public boolean add(Kevent<?> event) {
        if (!(event instanceof Kexplosion e)) {
            return false;
        }

        Vector v = e.getLocation().toVector();

        if (!this.nearby.isEmpty()) {
            this.nearby.remove(v);
        }

        if (!getEvent().getLocation().getWorld().equals(e.getLocation().getWorld())) {
            return false;
        } else if (!this.swinging && e.getLocation().toVector().equals(this.pos)) {
            this.increaseAmount();
            return true;
        }

        this.swinging = true;
        Vector v1 = this.pos.clone().subtract(getKinematicPlayer().getProperties().getExplosionTolerance());
        Vector v2 = this.pos.clone().add(getKinematicPlayer().getProperties().getExplosionTolerance());

        if (v.isInAABB(Vector.getMinimum(v1, v2), Vector.getMaximum(v1, v2))) {
            this.pos.add(v.subtract(this.pos).multiply(1.0 / (double) this.increaseAmount()));
            this.onGround = this.onGround && e.getEvent().getEntity().isOnGround();
            return true;
        }

        return false;
    }

    @Override
    public Location getLocation() {
        return getEvent().getLocation();
    }

    @Override
    public boolean isIgnored(KinematicsPlayer kplayer) {
        return this.onGround && !kplayer.isViewingAlert(AlertType.SHOW_ON_GROUND) || this.amount() < kplayer.getProperties().getMinExplosions();
    }

    @Override
    public TemplatedMessage template(KinematicsPlayer kplayer) {
        TextColor blockColour = switch (this.material) {
            case AIR, CAVE_AIR, VOID_AIR -> NamedTextColor.WHITE;
            case WATER -> NamedTextColor.BLUE;
            case LAVA -> TextColor.color(255, 100, 0);
            case SAND -> NamedTextColor.YELLOW;
            default -> NamedTextColor.LIGHT_PURPLE;
        };

        Vector displayPos = kplayer.isViewingAlert(AlertType.ENTITY_FEET) ? this.feet : this.pos;

        return new TemplatedMessage(
                kplayer.getKinematics().getConfiguration().explosionstemplate, displayPos, this.vel,
                this.tick(), this.order(), this.amount(), (this.amount() > 1) ? "explosions" : "explosion",
                blockColour
        );
    }

    @Override
    public AlertType getType() {
        return AlertType.EXPLOSION;
    }

    public Set<Vector> getNearbyEntities() {
        return this.nearby;
    }

    public void setNearbyEntities(Set<Vector> nearby) {
        this.nearby = nearby;
    }

    public Vector getPosition() {
        return this.pos;
    }

    public boolean isSwinging() {
        return this.swinging;
    }
}

