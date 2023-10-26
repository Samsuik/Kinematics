package bambi.kinematics.commands;

import bambi.kinematics.Kinematics;
import org.bukkit.entity.Player;

public class CommandShowOnGround extends ToggleCommand {
    public CommandShowOnGround() {
        super("showonground");
        this.addAlias("showground");
        this.addAlias("onground");
        this.addAlias("ground");
    }

    @Override
    public void toggle(Player p, int i) {
        Kinematics.setShowExplosionsonGround(this.toggleBool(Kinematics.isShowExplosionsonGround(), i));
        p.sendMessage(CommandShowOnGround.getPrefix() + "will " + (Kinematics.isShowExplosionsonGround() ? "" : "no longer ") + "show explosions on the ground");
    }
}

