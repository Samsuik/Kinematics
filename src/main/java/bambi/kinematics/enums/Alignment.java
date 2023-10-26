package bambi.kinematics.enums;

import org.bukkit.util.Vector;

public enum Alignment {
    NORTH(0.49f, Direction.z),
    EAST(0.51f, Direction.x),
    SOUTH(0.516f, Direction.z),
    WEST(0.49f, Direction.x),
    LEFT(0.49f, Direction.x),
    RIGHT(0.51f, Direction.x),
    TOP(0.01999998f, Direction.y),
    TRAP(-0.16750002f, Direction.y),
    SLAB(-0.48000002f, Direction.y),
    MID(0.5f, Direction.x),
    mid(0.5f, Direction.z);

    private final Vector vec;
    private final Direction direction;

    Alignment(float d, Direction dir) {
        this.vec = dir.setvec(new Vector().zero(), d);
        this.direction = dir;
    }

    public Vector getVector() {
        return this.vec;
    }

    public int getInt() {
        return this.direction.getInt();
    }

    public Direction getDirection() {
        return this.direction;
    }
}

