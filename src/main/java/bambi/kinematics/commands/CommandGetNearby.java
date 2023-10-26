package bambi.kinematics.commands;

import bambi.kinematics.Kinematics;
import org.bukkit.entity.Player;

public class CommandGetNearby extends ToggleCommand {
    public CommandGetNearby() {
        super("getnearby");
        this.addAlias("nearby");
        this.addAlias("near");
        this.addAlias("efficiency");
        this.addAlias("effi");
        this.addSub(new CommandMinNearby());
    }

    @Override
    public void toggle(Player p, int i) {
        this.toggleSet(Kinematics.getGetnearbyPlayer(), p, i);
        p.sendMessage(CommandGetNearby.getPrefix() + "will " + (Kinematics.getGetnearbyPlayer().contains(p) ? "" : "no longer ") + "show entities near explosions");
    }
}

