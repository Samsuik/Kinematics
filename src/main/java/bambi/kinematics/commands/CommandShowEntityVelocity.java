package bambi.kinematics.commands;

import bambi.kinematics.Kinematics;
import org.bukkit.entity.Player;

public class CommandShowEntityVelocity extends ToggleCommand {
    public CommandShowEntityVelocity() {
        super("showentityvelocity");
        this.addAlias("velocity");
        this.addAlias("vel");
    }

    @Override
    public void toggle(Player p, int i) {
        Kinematics.setShowEntityVelocity(this.toggleBool(Kinematics.isShowEntityVelocity(), i));
        p.sendMessage(CommandShowEntityVelocity.getPrefix() + "will " + (Kinematics.isShowEntityVelocity() ? "" : "no longer ") + "show entity velocity");
    }
}

