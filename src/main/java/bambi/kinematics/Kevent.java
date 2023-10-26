package bambi.kinematics;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityEvent;

import java.util.HashSet;

public abstract class Kevent {
    private int amount = 1;
    private final int tick;
    private final int order;

    public abstract boolean add(EntityEvent var1);

    public abstract boolean isIgnored();

    public abstract TextComponent toTextComponent();

    public abstract HashSet<Player> getPlayers();

    public Kevent(int tick2, int order) {
        this.tick = tick2;
        this.order = order;
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
}

