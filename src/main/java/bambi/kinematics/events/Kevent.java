package bambi.kinematics.events;

import bambi.kinematics.enums.AlertType;
import bambi.kinematics.player.KinematicsPlayer;
import bambi.kinematics.utils.TemplatedMessage;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityEvent;

public abstract class Kevent<E extends EntityEvent> {

    private final KinematicsPlayer kplayer;
    private final E event;

    private int amount = 1;
    private final int tick;
    private final int order;

    public Kevent(KinematicsPlayer kplayer, E event, int tick2, int order) {
        this.kplayer = kplayer;
        this.event = event;
        this.tick = tick2;
        this.order = order;
    }

    public final KinematicsPlayer getKinematicPlayer() {
        return kplayer;
    }

    public final E getEvent() {
        return event;
    }

    public int amount() {
        return this.amount;
    }

    public int increaseAmount() {
        return ++this.amount;
    }

    public int tick() {
        return this.tick;
    }

    public int order() {
        return this.order;
    }

    public abstract boolean add(Kevent<?> event);

    public abstract Location getLocation();

    public abstract boolean isIgnored(KinematicsPlayer kplayer);

    public abstract TemplatedMessage template(KinematicsPlayer kplayer);

    public abstract AlertType getType();
}

