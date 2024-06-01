package bambi.kinematics.commands;

import bambi.kinematics.Kinematics;
import bambi.kinematics.commands.integer.CommandDistance;
import bambi.kinematics.commands.integer.CommandRound;
import bambi.kinematics.commands.other.CommandChain;
import bambi.kinematics.commands.other.CommandKspawn;
import bambi.kinematics.commands.other.CommandTolerance;
import bambi.kinematics.commands.other.CommandWait;
import bambi.kinematics.commands.toggle.*;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.*;

public final class CommandKinematics extends KinematicsCommand {
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
        this.addSub(new CommandShowEntityFeet(plugin));
        this.addSub(new CommandChain(plugin, this));
        this.addSub(new CommandWait(plugin));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {}

    @Override
    public void execute(KinematicsPlayer kplayer, String[] args) {
        kplayer.sendPrefixedMessage(Component.text("available commands:").append(createHelpMessage()));
    }

    private TextComponent createHelpMessage() {
        TextComponent.Builder builder = Component.text();
        Set<Class<?>> discovered = new HashSet<>();

        for (KinematicsCommand subcommand : getSubCommands()) {
            if (discovered.contains(subcommand.getClass()) || !subcommand.showInHelpMessage()) {
                continue;
            }

            builder.append(Component.newline())
                    .append(Component.text(" > ", NamedTextColor.AQUA))
                    .append(Component.text(subcommand.getName()));

            if (subcommand.description() != null) {
                builder.append(Component.text("; " + subcommand.description(), NamedTextColor.GRAY));
            }

            for (KinematicsCommand more : subcommand.getSubCommands()) {
                discovered.add(more.getClass());
            }

            discovered.add(subcommand.getClass());
        }

        return builder.build();
    }
}

