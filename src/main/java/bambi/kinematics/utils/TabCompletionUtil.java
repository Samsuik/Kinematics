package bambi.kinematics.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.Nullable;

public final class TabCompletionUtil {
    private static boolean MODERN_RAYTRACE = true;

    static {
        try {
            Class.forName("org.bukkit.util.RayTraceResult");
        } catch (ClassNotFoundException cnfe) {
            MODERN_RAYTRACE = false;
        }
    }

    private TabCompletionUtil() {}

    public static Location getBlockLocation(Player player) {
        Location location;
        if (MODERN_RAYTRACE) {
            location = getRelativeTargetBlockLocation(player);
        } else {
            location = getTargetBlockLocation(player);
        }

        if (location == null) {
            location = player.getLocation();
        }

        return blockLocation(location);
    }

    private static @Nullable Location getTargetBlockLocation(Player player) {
        Block block = player.getTargetBlock(null, 9);
        if (block.getLocation().distanceSquared(player.getLocation()) >= 8*8) {
            return null;
        }
        return block.getLocation();
    }

    private static @Nullable Location getRelativeTargetBlockLocation(Player player) {
        RayTraceResult result = player.rayTraceBlocks(8);

        Location location = null;
        if (result != null) {
            Block relative = result.getHitBlock().getRelative(result.getHitBlockFace());
            location = relative.getLocation();
        }

        return location;
    }

    private static Location blockLocation(Location location) {
        return new Location(location.getWorld(), location.getBlockX() + 0.5, location.getBlockY(), location.getBlockZ() + 0.5);
    }
}
