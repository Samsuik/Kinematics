package bambi.kinematics.commands;

import bambi.kinematics.Kinematics;
import org.bukkit.entity.Player;

public class CommandProtection extends ToggleCommand {
    public CommandProtection() {
        super("protection");
    }

    @Override
    public void toggle(Player p, int i) {
        Kinematics.setProtection(this.toggleBool(Kinematics.isProtection(), i));
        p.sendMessage(CommandProtection.getPrefix() + "will " + (Kinematics.isProtection() ? "" : "no longer ") + "protect blocks");
    }
}

