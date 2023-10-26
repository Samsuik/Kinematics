package bambi.kinematics;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;

public class Kexplosion extends Kevent {
    private final EntityExplodeEvent event;
    private final Vector pos;
    private boolean onGround;
    private final HashSet<Vector> nearby = new HashSet<>();
    private final Vector vel;
    private final Material material;
    private boolean swinging = false;

    public Kexplosion(EntityExplodeEvent e, int tick2, int order) {
        super(tick2, order);
        this.event = e;
        this.pos = this.event.getLocation().toVector();
        this.onGround = e.getEntity().isOnGround();
        for (Entity entity : e.getEntity().getNearbyEntities(8.0, 8.0, 8.0)) {
            if (!entity.getType().equals(EntityType.PRIMED_TNT) && !entity.getType().equals(EntityType.FALLING_BLOCK))
                continue;
            this.nearby.add(entity.getLocation().toVector().add(Util.entitycenter));
        }
        this.vel = e.getEntity().getVelocity();
        this.material = e.getLocation().getBlock().getType();
    }

    @Override
    public boolean add(EntityEvent event) {
        if (!(event instanceof EntityExplodeEvent e)) {
            return false;
        }

        Vector v = e.getLocation().toVector();
        this.nearby.remove(v);

        if (!this.event.getLocation().getWorld().equals(e.getLocation().getWorld())) {
            return false;
        } else if (!this.swinging && e.getLocation().toVector().equals(this.pos)) {
            this.increaseAmount();
            return true;
        }

        this.swinging = true;
        Vector v1 = this.pos.clone().subtract(Kinematics.getexplosionTolerance());
        Vector v2 = this.pos.clone().add(Kinematics.getexplosionTolerance());

        if (v.isInAABB(Vector.getMinimum(v1, v2), Vector.getMaximum(v1, v2))) {
            this.pos.add(v.subtract(this.pos).multiply(1.0 / (double) this.increaseAmount()));
            this.onGround = this.onGround && e.getEntity().isOnGround();
            return true;
        }

        return false;
    }

    @Override
    public boolean isIgnored() {
        return this.onGround && !Kinematics.isShowExplosionsonGround() || this.amount() < Kinematics.getMinExplosions();
    }

    @Override
    public TextComponent toTextComponent() {
        String s = Util.replaceAll(Kinematics.getExplosionstemplate(), this.pos, this.vel, this.tick(), this.order(), this.amount(), this.amount() > 1 ? "explosions" : "explosion");
        TextComponent tc = Util.tpClickEvent(new TextComponent(s), this.pos);

        switch (this.material) {
            case WATER, LEGACY_STATIONARY_WATER -> tc.setColor(ChatColor.BLUE);
            case SAND -> tc.setColor(ChatColor.YELLOW);
            case AIR -> tc.setColor(ChatColor.WHITE);
            default -> tc.setColor(ChatColor.LIGHT_PURPLE);
        }

        return tc;
    }

    @Override
    public HashSet<Player> getPlayers() {
        return Kinematics.getExplosionPlayerSet();
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public HashSet<Vector> getnearby() {
        return this.nearby;
    }

    public Vector getpos() {
        return this.pos;
    }

    public boolean isSwinging() {
        return this.swinging;
    }
}

