package bambi.kinematics.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandKinematics extends KinematicsCommand {
    public CommandKinematics() {
        super("kinematics");
        this.addSub(new CommandExplosions());
        this.addSub(new CommandShowOnGround());
        this.addSub(new CommandRound());
        this.addSub(new CommandStacking());
        this.addSub(new CommandTolerance());
        this.addSub(new CommandGetNearby());
        this.addSub(new CommandKspawn());
        this.addSub(new CommandEntitySpawn());
        this.addSub(new CommandProtection());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void execute(Player p, String[] args) {
        p.sendMessage("Help");
    }
}

