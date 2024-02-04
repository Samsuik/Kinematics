package bambi.kinematics.commands.toggle;

import bambi.kinematics.Kinematics;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;

public class CommandProtection extends ToggleCommand {
    public CommandProtection(Kinematics plugin) {
        super(plugin, "protection");
    }

    @Override
    public void toggle(KinematicsPlayer kplayer, int i) {
        getConfig().protection = toggleBool(getConfig().protection, i);
        kplayer.sendPrefixedMessage(Component.text( "will " + (getConfig().protection ? "" : "no longer ") + "protect blocks"));
    }
}

