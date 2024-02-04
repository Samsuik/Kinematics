package bambi.kinematics.enums;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public enum Direction {
    x(0) {
        @Override
        public Vector setvec(Vector vec, double d) {
            return vec.setX(d);
        }

        @Override
        public Vector addvec(Vector vec, double d) {
            return vec.setX(vec.getX() + d);
        }

        @Override
        public Location addLoc(Location loc, double d) {
            return loc.add(d, 0, 0);
        }
    },
    y(1) {
        @Override
        public Vector setvec(Vector vec, double d) {
            return vec.setY(d);
        }

        @Override
        public Vector addvec(Vector vec, double d) {
            return vec.setY(vec.getY() + d);
        }

        @Override
        public Location addLoc(Location loc, double d) {
            return loc.add(0, d, 0);
        }
    },
    z(2) {
        @Override
        public Vector setvec(Vector vec, double d) {
            return vec.setZ(d);
        }

        @Override
        public Vector addvec(Vector vec, double d) {
            return vec.setZ(vec.getZ() + d);
        }

        @Override
        public Location addLoc(Location loc, double d) {
            return loc.add(0, 0, d);
        }
    };

    private final int dir;

    Direction(int i) {
        this.dir = i;
    }

    public final int getInt() {
        return this.dir;
    }

    public abstract Vector setvec(Vector vec, double d);

    public abstract Vector addvec(Vector vec, double d);

    public abstract Location addLoc(Location loc, double d);
}

