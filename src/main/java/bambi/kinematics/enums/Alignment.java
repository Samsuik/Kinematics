package bambi.kinematics.enums;

import org.bukkit.util.Vector;

public enum Alignment {
    NORTH(0.49f, Direction.Z),
    EAST(0.51f, Direction.X),
    SOUTH(0.516f, Direction.Z),
    WEST(0.49f, Direction.X),
    LEFT(0.49f, Direction.X),
    RIGHT(0.51f, Direction.X),
    TOP(0.01999998f, Direction.Y),
    TRAP(-0.16750002f, Direction.Y),
    SLAB(-0.48000002f, Direction.Y),
    MID(0.5f, Direction.X),
    mid(0.5f, Direction.Z);

    private final Vector vec;
    private final Direction direction;

    Alignment(float d, Direction dir) {
        this.vec = dir.setVec(new Vector().zero(), d);
        this.direction = dir;
    }

    public Vector getVector() {
        return this.vec;
    }

    public int directionIndex() {
        return this.direction.ordinal();
    }
}

