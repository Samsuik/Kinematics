package bambi.kinematics.commands;

import bambi.kinematics.Kinematics;
import bambi.kinematics.enums.Alignment;
import bambi.kinematics.enums.Direction;
import bambi.kinematics.player.KinematicsPlayer;
import bambi.kinematics.utils.ArrayUtil;
import bambi.kinematics.utils.Materials;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Locale;

public class CommandKspawn extends KinematicsCommand {
    public CommandKspawn(Kinematics plugin) {
        super(plugin, "kspawn", List.of("spawn"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {
        if (!(sender instanceof BlockCommandSender)) {
            throw new CommandException("player or commandblock command only");
        }
        this.execute(((BlockCommandSender) sender).getBlock().getWorld(), args);
    }

    @Override
    public void execute(KinematicsPlayer kplayer, String[] args) throws CommandException {
        if (kplayer.getPlayer().hasPermission("kinematics.kspawn")) {
            this.execute(kplayer.getPlayer().getWorld(), args);
        }
    }

    private void execute(World w, String[] args) throws CommandException {
        if (args.length < 4) {
            throw new CommandException("not enough arguments");
        }

        Vector vec = new Vector();
        Material material = Materials.matchClosestMaterial(args[0]);
        int amount = args.length > 4 ? CommandKspawn.parsInt(args[4]) : 1;
        int fuse = args.length > 5 ? CommandKspawn.parsInt(args[5]) : 80;

        for (Alignment alignment : Alignment.values()) {
            int coordIndex = alignment.directionIndex() + 1;
            String arg = args[coordIndex];
            if (arg.endsWith(alignment.name().toLowerCase())) {
                vec.add(alignment.getVector());
                args[coordIndex] = arg.substring(0, arg.length() - alignment.name().length());
            }
        }

        for (Direction dir : Direction.values()) {
            dir.addVec(vec, CommandKspawn.parsDouble(args[dir.ordinal() + 1]));
        }

        Location loc = vec.toLocation(w);

        for (int i = 0; i < amount; ++i) {
            if (material == Material.TNT) {
                TNTPrimed tnt = (TNTPrimed) w.spawnEntity(loc, EntityType.PRIMED_TNT);
                tnt.setVelocity(new Vector().zero());
                tnt.setFuseTicks(fuse);
            } else if (material != null && material.hasGravity()) {
                FallingBlock fb = (FallingBlock) w.spawnEntity(loc, EntityType.FALLING_BLOCK);
                fb.setTicksLived(1);
                fb.setBlockData(material.createBlockData());
                fb.teleport(loc);
            } else {
                throw new CommandException("unable to get entity: " + args[0].toLowerCase(Locale.ENGLISH));
            }
        }
    }

    @Override
    public List<String> tabComplete(KinematicsPlayer kplayer, String fullCommand, String[] args) {
        if (args.length <= 1) {
            return List.of("tnt", "sand", "redsand", "whiteconcrete");
        }

        Location location = kplayer.getTabCompletionBlockLocation();
        return List.of(location.x() + " " + location.y() + " " + location.z());
    }
}

