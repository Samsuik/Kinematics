package bambi.kinematics.commands;

import bambi.kinematics.Kinematics;
import org.bukkit.command.CommandSender;

public class CommandMinExplosions extends IntegerCommand {
    public CommandMinExplosions() {
        super("minexplosions");
        this.addAlias("min");
        this.addAlias("minimum");
    }

    @Override
    public void execute(CommandSender sender, int i) {
        Kinematics.setMinExplosions(i);
    }

    @Override
    public void sendMessage(CommandSender sender) {
        sender.sendMessage(CommandMinExplosions.getPrefix() + "will show " + Kinematics.getMinExplosions() + " explosions or more");
    }
}

