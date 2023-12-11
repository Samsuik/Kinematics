package bambi.kinematics;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.util.Vector;

public class Util {
    public static final Vector entitycenter = new Vector(0.0f, 0.49f, 0.0f);

    public static double round(double a) {
        return Util.round(a, Kinematics.getDecimals());
    }

    public static double round(double a, int b) {
        if (b == -1) {
            return a;
        }
        return (double) Math.round(Math.pow(10.0, b) * a) / Math.pow(10.0, b);
    }

    public static String vectoString(Vector vec) {
        return Util.vectoString(vec, Kinematics.getDecimals());
    }

    public static String vectoString(Vector vec, int round) {
        return "x:" + Util.round(vec.getX(), round) + " y:" + Util.round(vec.getY(), round) + " z:" + Util.round(vec.getZ(), round);
    }

    public static String replaceVec(Vector vec, String s) {
        s = s.replaceAll("@x", String.valueOf(Util.round(vec.getX()))).replaceAll("@y", String.valueOf(Util.round(vec.getY()))).replaceAll("@z", String.valueOf(Util.round(vec.getZ())));
        return s;
    }

    public static String replaceVel(Vector vec, String s) {
        s = s.replaceAll("@vx0", String.valueOf(Util.round(vec.getX()))).replaceAll("@vy0", String.valueOf(Util.round(vec.getY()))).replaceAll("@vz0", String.valueOf(Util.round(vec.getZ()))).replaceAll("@vx", vec.getX() == 0.0 ? "" : "(" + Util.round(vec.getX()) + ")").replaceAll("@vy", vec.getY() == 0.0 ? "" : "(" + Util.round(vec.getY()) + ")").replaceAll("@vz", vec.getZ() == 0.0 ? "" : "(" + Util.round(vec.getZ()) + ")");
        return s;
    }

    public static String replaceAll(String s, Vector pos, Vector vel, int tick2, int order, int amount, String type) {
        // place holders
        s = s.replaceAll("@tick", tick2 < 0 ? "" : String.valueOf(tick2))
            .replaceAll("@order", order < 0 ? "" : String.valueOf(order))
            .replaceAll("@amount", amount < 0 ? "" : String.valueOf(amount))
            .replaceAll("@type", type)
            .replaceAll("@x", String.valueOf(Util.round(pos.getX())))
            .replaceAll("@y", String.valueOf(Util.round(pos.getY())))
            .replaceAll("@z", String.valueOf(Util.round(pos.getZ())));

        if (Kinematics.isShowEntityVelocity()) {
            s = s.replaceAll("@vx", vel.getX() == 0.0 ? "" : "(" + Util.round(vel.getX()) + ")")
                .replaceAll("@vy", vel.getY() == 0.0 ? "" : "(" + Util.round(vel.getY()) + ")")
                .replaceAll("@vz", vel.getZ() == 0.0 ? "" : "(" + Util.round(vel.getZ()) + ")");
        } else {
            s = s.replaceAll("@vx", "")
                .replaceAll("@vy", "")
                .replaceAll("@vz", "");
        }

        return s;
    }

    public static TextComponent tpClickEvent(TextComponent tc, Vector tp) {
        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/minecraft:tp " + tp.getX() + " " + tp.getY() + " " + tp.getZ()));
        return tc;
    }
}

