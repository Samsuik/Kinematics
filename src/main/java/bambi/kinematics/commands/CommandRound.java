package bambi.kinematics.commands;

import bambi.kinematics.Kinematics;
import org.bukkit.command.CommandSender;

public class CommandRound extends IntegerCommand {
    public CommandRound() {
        super("round");
        this.addAlias("decimals");
    }

    @Override
    public void execute(CommandSender sender, int i) {
        Kinematics.setDecimals(i);
    }

    @Override
    public void sendMessage(CommandSender sender) {
        sender.sendMessage(CommandRound.getPrefix() + "will round to " + Kinematics.getDecimals() + "decimals");
    }
}

