package bambi.kinematics.commands.toggle;

import bambi.kinematics.Kinematics;
import bambi.kinematics.commands.other.CommandTolerance;
import bambi.kinematics.commands.integer.CommandMinExplosions;
import bambi.kinematics.enums.AlertType;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandExplosions extends ToggleCommand {
    public CommandExplosions(Kinematics plugin) {
        super(plugin, "explosions", List.of("explosion", "exp", "x"));
        this.addSub(new CommandShowOnGround(plugin));
        this.addSub(new CommandShowEntityVelocity(plugin));
        this.addSub(new CommandMinExplosions(plugin));
        this.addSub(new CommandTolerance(plugin));
        this.addSub(new CommandGetNearby(plugin));
        this.addSub(new CommandShowEntityFeet(plugin));
    }

    @Override
    public @Nullable String description() {
        return "explosion alerts";
    }

    @Override
    public void toggle(KinematicsPlayer kplayer, int i) {
        kplayer.toggleAlert(AlertType.EXPLOSION, i);
        kplayer.sendPrefixedMessage(Component.text( "will " + (kplayer.isViewingAlert(AlertType.EXPLOSION) ? "" : "no longer ") + "show explosions"));
    }
}

