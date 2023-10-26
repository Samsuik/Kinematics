package bambi.kinematics.commands;

import bambi.kinematics.Kinematics;
import org.bukkit.command.CommandSender;

public class CommandMinNearby extends IntegerCommand {
    public CommandMinNearby() {
        super("minnearby");
        this.addAlias("min");
        this.addAlias("minimum");
    }

    @Override
    public void execute(CommandSender sender, int i) {
        Kinematics.setMinNearby(i);
    }

    @Override
    public void sendMessage(CommandSender sender) {
        sender.sendMessage(CommandMinNearby.getPrefix() + "will show nearby entities at" + Kinematics.getMinNearby() + " explosions or more");
    }
}

