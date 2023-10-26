package bambi.kinematics.commands;

import bambi.kinematics.Kinematics;
import org.bukkit.entity.Player;

public class CommandEntitySpawn extends ToggleCommand {
    public CommandEntitySpawn() {
        super("entityspawn");
        this.addAlias("entityinfo");
        this.addAlias("info");
    }

    @Override
    public void toggle(Player p, int i) {
        this.toggleSet(Kinematics.getEntityspawnPlayerSet(), p, i);
        p.sendMessage(CommandEntitySpawn.getPrefix() + "will " + (Kinematics.getEntityspawnPlayerSet().contains(p) ? "" : "no longer ") + "show entity spawning");
    }
}

