package bambi.kinematics.utils;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public final class Materials {
    private Materials() {}

    private final static Predicate<Material> IS_CANNON_ENTITY_MATERIAL = material -> material.hasGravity() || material == Material.TNT;
    private final static List<Material> CANNON_ENTITY_MATERIALS = new ArrayList<>();

    static {
        for (Material material : Material.values()) {
            if (IS_CANNON_ENTITY_MATERIAL.test(material)) {
                CANNON_ENTITY_MATERIALS.add(material);
            }
        }
    }

    public static @Nullable Material matchClosestMaterial(String arg) {
        String uppercaseArg = arg.toUpperCase(Locale.ENGLISH);
        for (Material material : CANNON_ENTITY_MATERIALS) {
            if (material.name().startsWith(uppercaseArg)) {
                return material;
            }
        }
        return materialByDistance(uppercaseArg);

    }

    private static @Nullable Material materialByDistance(String in) {
        int materialDistance = Integer.MAX_VALUE;
        Material closestMaterial = null;
        for (Material material : CANNON_ENTITY_MATERIALS) {
            int distance = StringUtils.getLevenshteinDistance(in, material.name());
            if (distance < materialDistance) {
                closestMaterial = material;
                materialDistance = distance;
            }
        }
        return closestMaterial;
    }
}
