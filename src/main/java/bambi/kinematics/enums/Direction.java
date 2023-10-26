package bambi.kinematics.enums;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public enum Direction {
    x(0),
    y(1),
    z(2);

    private final int dir;

    Direction(int i) {
        this.dir = i;
    }

    public int getInt() {
        return this.dir;
    }

    public double getvec(Vector vec) {
        switch (this.dir) {
            case 0: {
                return vec.getX();
            }
            case 1: {
                return vec.getY();
            }
            case 2: {
                return vec.getZ();
            }
        }
        return 0.0;
    }

    public Vector setvec(Vector vec, double d) {
        switch (this.dir) {
            case 0: {
                vec.setX(d);
                break;
            }
            case 1: {
                vec.setY(d);
                break;
            }
            case 2: {
                vec.setZ(d);
                break;
            }
        }
        return vec;
    }

    public Vector addvec(Vector vec, double d) {
        switch (this.dir) {
            case 0: {
                vec.setX(vec.getX() + d);
                break;
            }
            case 1: {
                vec.setY(vec.getY() + d);
                break;
            }
            case 2: {
                vec.setZ(vec.getZ() + d);
                break;
            }
        }
        return vec;
    }

    public Location setLoc(Location loc, double d) {
        switch (this.dir) {
            case 0: {
                loc.setX(d);
                break;
            }
            case 1: {
                loc.setY(d);
                break;
            }
            case 2: {
                loc.setZ(d);
                break;
            }
        }
        return loc;
    }

    public Location addLoc(Location loc, double d) {
        switch (this.dir) {
            case 0: {
                loc.setX(loc.getX() + d);
                break;
            }
            case 1: {
                loc.setY(loc.getY() + d);
                break;
            }
            case 2: {
                loc.setZ(loc.getZ() + d);
                break;
            }
        }
        return loc;
    }
}

