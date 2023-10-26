package bambi.kinematics.commands;

import bambi.kinematics.Kinematics;
import org.bukkit.entity.Player;

public class CommandStacking extends ToggleCommand {
    public CommandStacking() {
        super("stacking");
        this.addAlias("stacks");
        this.addAlias("stack");
    }

    @Override
    public void toggle(Player p, int i) {
        this.toggleSet(Kinematics.getStackPlayerSet(), p, i);
        p.sendMessage(CommandStacking.getPrefix() + "will " + (Kinematics.getStackPlayerSet().contains(p) ? "" : "no longer ") + "show stacking");
    }
}

