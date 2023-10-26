package bambi.kinematics.commands;

import bambi.kinematics.Kinematics;
import org.bukkit.command.CommandSender;

public class CommandMinStacking extends IntegerCommand {
    public CommandMinStacking() {
        super("minstacking");
        this.addAlias("min");
        this.addAlias("minimum");
    }

    @Override
    public void execute(CommandSender sender, int i) {
        Kinematics.setMinStacking(i);
    }

    @Override
    public void sendMessage(CommandSender sender) {
        sender.sendMessage(CommandMinStacking.getPrefix() + "will show " + Kinematics.getMinStacking() + " stacking or more");
    }
}

