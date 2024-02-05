package bambi.kinematics.player;

import org.bukkit.util.Vector;

/**
 * SANITY WARNING: This class contains fields that were once a global state in Kinematics. To honor their retirement
 * they are now located here out of the prying eye. I hope you will also honor this by closing this tab in your ide.
 */
public final class Properties {
    private Vector explosionTolerance = new Vector(0.0, 2.0, 0.0);
    private int minExplosions;
    private int minStacking;
    private int minNearby = 100;
    private int ticksLived;
    private int decimals = 4;
    private int distance;
    private int order;

    public Vector getExplosionTolerance() {
        return explosionTolerance;
    }

    public void setExplosionTolerance(Vector explosionTolerance) {
        this.explosionTolerance = explosionTolerance;
    }

    public int getMinExplosions() {
        return minExplosions;
    }

    public void setMinExplosions(int minExplosions) {
        this.minExplosions = minExplosions;
    }

    public int getMinStacking() {
        return minStacking;
    }

    public void setMinStacking(int minStacking) {
        this.minStacking = minStacking;
    }

    public int getMinNearby() {
        return minNearby;
    }

    public void setMinNearby(int minNearby) {
        this.minNearby = minNearby;
    }

    public int getTicksLived() {
        return ticksLived;
    }

    public void setTicksLived(int ticksLived) {
        this.ticksLived = ticksLived;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
