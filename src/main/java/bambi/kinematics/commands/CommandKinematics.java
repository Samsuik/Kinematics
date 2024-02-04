package bambi.kinematics.commands;

import bambi.kinematics.Kinematics;
import bambi.kinematics.commands.integer.CommandDistance;
import bambi.kinematics.commands.integer.CommandRound;
import bambi.kinematics.commands.toggle.*;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public class CommandKinematics extends KinematicsCommand {
    public CommandKinematics(Kinematics plugin) {
        super(plugin, "kinematics");
        this.addSub(new CommandExplosions(plugin));
        this.addSub(new CommandShowOnGround(plugin));
        this.addSub(new CommandShowEntityVelocity(plugin));
        this.addSub(new CommandRound(plugin));
        this.addSub(new CommandStacking(plugin));
        this.addSub(new CommandTolerance(plugin));
        this.addSub(new CommandGetNearby(plugin));
        this.addSub(new CommandKspawn(plugin));
        this.addSub(new CommandEntitySpawn(plugin));
        this.addSub(new CommandProtection(plugin));
        this.addSub(new CommandDistance(plugin));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {}

    @Override
    public void execute(KinematicsPlayer kplayer, String[] args) {
        kplayer.sendPrefixedMessage(Component.text("help i'm stuck inside the screen"));
    }
}

