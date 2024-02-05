package bambi.kinematics.configuration;

import bambi.kinematics.Kinematics;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class Configuration {
    private final FileConfiguration internalConfig;
    public Component prefix;
    public Component explosionstemplate;
    public Component stackingtemplate;
    public Component getnearbytemplate;
    public Component entityspawntemplate;
    public boolean protection;
    public Set<Material> notProtected;
    public int maxAlertDistance;

    public Configuration(Kinematics plugin) {
        internalConfig = plugin.getConfig();

        prefix = getMiniComponent("templates.prefix", "<aqua>Kinematics</aqua> <gray>");
        explosionstemplate = getComponent("templates.explosions", "[@tick:@order] @amount @type at x:@x@vx y:@y@vy z:@z@vz");
        stackingtemplate = getComponent("templates.stacking", "[@tick:@order] @amount @type stacked at x:@x@vx y:@y@vy z:@z@vz");
        getnearbytemplate = getComponent("templates.nearby", "Entity near power: x:@x@vx y:@y@vy z:@z@vz d:@d@vd");
        entityspawntemplate = getComponent("templates.entityspawn", "[@tick] @type spawned at x:@x y:@y z:@z");
        protection = getBoolean("protection.enable", true);
        notProtected = getMaterials("protection.not_protected", List.of(Material.COBBLESTONE, Material.SAND, Material.GRAVEL, Material.COBBLESTONE_SLAB, Material.STONE_SLAB));
        maxAlertDistance = getInt("alert-distance", 1024);

        internalConfig.options().copyDefaults(true);
        plugin.saveConfig();
    }

    private Component getMiniComponent(String path, String def, String... comments) {
        return MiniMessage.miniMessage().deserialize(getString(path, def, comments));
    }

    private Component getComponent(String path, String def, String... comments) {
        return Component.text(getString(path, def, comments));
    }

    private String getString(String path, String def, String... comments) {
        updateDefaultOf(path, def, comments);
        return internalConfig.getString(path, def);
    }

    private boolean getBoolean(String path, boolean def, String... comments) {
        updateDefaultOf(path, def, comments);
        return internalConfig.getBoolean(path, def);
    }

    private int getInt(String path, int def, String... comments) {
        updateDefaultOf(path, def, comments);
        return internalConfig.getInt(path, def);
    }

    private Set<Material> getMaterials(String path, List<Material> materials, String... comments) {
        List<String> materialNames = materials.stream()
                .map(Enum::name)
                .toList();

        updateDefaultOf(path, materialNames, comments);

        return internalConfig.getList(path, materials).stream()
                .filter(obj -> obj instanceof String)
                .map(obj -> (String) obj)
                .map(Material::matchMaterial)
                .collect(Collectors.toSet());
    }

    private void updateDefaultOf(String path, Object with, String... comments) {
        internalConfig.addDefault(path, with);

        if (comments.length > 0) {
            internalConfig.setComments(path, List.of(comments));
        }
    }
}
