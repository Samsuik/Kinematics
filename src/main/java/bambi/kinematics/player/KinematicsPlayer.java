package bambi.kinematics.player;

import bambi.kinematics.Kinematics;
import bambi.kinematics.enums.AlertType;
import bambi.kinematics.utils.TemplatedMessage;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import java.util.EnumSet;
import java.util.Set;

public final class KinematicsPlayer {
    private final Kinematics kinematics;
    private final Player player;
    private final Audience audience;
    private final Set<AlertType> displayAlerts = EnumSet.noneOf(AlertType.class);
    private final Properties properties = new Properties();

    public KinematicsPlayer(Kinematics kinematics, Player player) {
        this.kinematics = kinematics;
        this.player = player;
        this.audience = kinematics.getAdventure().player(player);
        this.properties.setDistance(kinematics.getConfiguration().maxAlertDistance);
        this.displayAlerts.add(AlertType.SHOW_ENTITY_VELOCITY);
        this.displayAlerts.add(AlertType.SHOW_ON_GROUND);
    }

    public Kinematics getKinematics() {
        return kinematics;
    }

    public Player getPlayer() {
        return player;
    }

    public Audience getAudience() {
        return audience;
    }

    public Properties getProperties() {
        return properties;
    }

    public boolean isViewingAlert(AlertType type) {
        return displayAlerts.contains(type);
    }

    public void toggleAlert(AlertType type, int i) {
        switch (i) {
            case -1 -> toggleAlert(type);
            case 0 -> displayAlerts.remove(type);
            case 1 -> displayAlerts.add(type);
        }
    }

    public void toggleAlert(AlertType type) {
        if (!displayAlerts.remove(type)) {
            displayAlerts.add(type);
        }
    }

    public boolean isInAlertDistance(Location location) {
        // actually stupid that these world checks are required
        return location.getWorld() != null && player.getWorld().equals(location.getWorld())
            && player.getLocation().distanceSquared(location) < Math.pow(properties.getDistance(), 2);
    }

    public void sendPrefixedMessage(Component component) {
        audience.sendMessage(kinematics.getConfiguration().prefix.append(component));
    }

    public void sendTemplatedMessage(TemplatedMessage template) {
        audience.sendMessage(template.componentFor(this));
    }

    public Location getTabCompletionBlockLocation() {
        RayTraceResult result = player.rayTraceBlocks(8);

        Location location;
        if (result == null) {
            location = player.getLocation(); // failed
        } else {
            Block relative = result.getHitBlock().getRelative(result.getHitBlockFace());
            location = relative.getLocation();
        }

        return location.toBlockLocation().add(0.5, 0.0, 0.5);
    }
}
