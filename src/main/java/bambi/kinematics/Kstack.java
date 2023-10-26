package bambi.kinematics;

import bambi.kinematics.enums.Direction;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;

public class Kstack extends Kevent {
    private final EntityChangeBlockEvent event;
    private final Vector vel;
    private Location next;

    public Kstack(EntityChangeBlockEvent e, int tick2, int order) {
        super(tick2, order);
        this.event = e;
        this.vel = this.event.getEntity().getVelocity();
        this.next = Direction.y.addLoc(this.event.getBlock().getLocation(), 1.0);
    }

    @Override
    public boolean add(EntityEvent event) {
        if (!(event instanceof EntityChangeBlockEvent e)) {
            return false;
        } else if (e.getBlock().getLocation().equals(this.next)) {
            this.next = Direction.y.addLoc(this.next, 1.0);
            this.increaseAmount();
            return true;
        }

        return false;
    }

    @Override
    public boolean isIgnored() {
        return this.amount() < Kinematics.getMinStacking();
    }

    @Override
    public TextComponent toTextComponent() {
        String s = Kinematics.getStackingtemplate();
        s = Util.replaceAll(s, this.event.getBlock().getLocation().toVector(), this.vel, this.tick(), this.order(), this.amount(), this.event.getBlock().getType().name().toLowerCase());
        TextComponent tc = Util.tpClickEvent(new TextComponent(s), Direction.y.setvec(this.event.getBlock().getLocation().toVector(), this.event.getBlock().getLocation().getY()));
        return tc;
    }

    @Override
    public HashSet<Player> getPlayers() {
        return Kinematics.getStackPlayerSet();
    }
}

