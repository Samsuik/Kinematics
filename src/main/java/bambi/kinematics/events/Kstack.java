package bambi.kinematics.events;

import bambi.kinematics.enums.AlertType;
import bambi.kinematics.enums.Direction;
import bambi.kinematics.player.KinematicsPlayer;
import bambi.kinematics.utils.TemplatedMessage;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.util.Vector;

public final class Kstack extends Kevent<EntityChangeBlockEvent> {
    private final Vector vel;
    private Location next;

    public Kstack(KinematicsPlayer kplayer, EntityChangeBlockEvent e, int tick2, int order) {
        super(kplayer, e, tick2, order);
        this.vel = e.getEntity().getVelocity();
        this.next = Direction.y.addLoc(e.getBlock().getLocation(), 1.0);
    }

    @Override
    public boolean add(Kevent<?> event) {
        if (!(event instanceof Kstack e)) {
            return false;
        } else if (e.getEvent().getBlock().getLocation().equals(this.next)) {
            this.next = Direction.y.addLoc(this.next, 1.0);
            this.increaseAmount();
            return true;
        }

        return false;
    }

    @Override
    public Location getLocation() {
        return getEvent().getBlock().getLocation();
    }

    @Override
    public boolean isIgnored(KinematicsPlayer kplayer) {
        return this.amount() < kplayer.getProperties().getMinStacking();
    }

    @Override
    public TemplatedMessage template(KinematicsPlayer kplayer) {
        return new TemplatedMessage(
                kplayer.getKinematics().getConfiguration().stackingtemplate, getEvent().getBlock().getLocation().toVector(),
                this.vel, this.tick(), this.order(), this.amount(), getEvent().getBlock().getType().name().toLowerCase(),
                NamedTextColor.WHITE
        );
    }

    @Override
    public AlertType getType() {
        return AlertType.STACK;
    }
}

