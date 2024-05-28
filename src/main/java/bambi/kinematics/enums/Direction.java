package bambi.kinematics.enums;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public enum Direction {
    X {
        @Override
        public Vector setVec(Vector vec, double d) {
            return vec.setX(d);
        }

        @Override
        public Vector addVec(Vector vec, double d) {
            return vec.setX(vec.getX() + d);
        }

        @Override
        public Location addLoc(Location loc, double d) {
            return loc.add(d, 0, 0);
        }
    },
    Y {
        @Override
        public Vector setVec(Vector vec, double d) {
            return vec.setY(d);
        }

        @Override
        public Vector addVec(Vector vec, double d) {
            return vec.setY(vec.getY() + d);
        }

        @Override
        public Location addLoc(Location loc, double d) {
            return loc.add(0, d, 0);
        }
    },
    Z {
        @Override
        public Vector setVec(Vector vec, double d) {
            return vec.setZ(d);
        }

        @Override
        public Vector addVec(Vector vec, double d) {
            return vec.setZ(vec.getZ() + d);
        }

        @Override
        public Location addLoc(Location loc, double d) {
            return loc.add(0, 0, d);
        }
    };

    public abstract Vector setVec(Vector vec, double d);

    public abstract Vector addVec(Vector vec, double d);

    public abstract Location addLoc(Location loc, double d);
}

