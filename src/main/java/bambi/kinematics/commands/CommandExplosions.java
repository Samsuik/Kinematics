package bambi.kinematics.commands;

import bambi.kinematics.Kinematics;
import org.bukkit.entity.Player;

public class CommandExplosions extends ToggleCommand {
    public CommandExplosions() {
        super("explosions");
        this.addAlias("explosion");
        this.addAlias("exp");
        this.addAlias("x");
        this.addSub(new CommandShowOnGround());
        this.addSub(new CommandMinExplosions());
        this.addSub(new CommandTolerance());
        this.addSub(new CommandGetNearby());
    }

    @Override
    public void toggle(Player p, int i) {
        this.toggleSet(Kinematics.getExplosionPlayerSet(), p, i);
        p.sendMessage(CommandExplosions.getPrefix() + "will " + (Kinematics.getExplosionPlayerSet().contains(p) ? "" : "no longer ") + "show explosions");
    }
}

