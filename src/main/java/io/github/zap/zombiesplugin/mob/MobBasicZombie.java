package io.github.zap.zombiesplugin.mob;

import io.github.zap.zombiesplugin.pathfinder.PathfinderGoalEscapeWindow;
import io.github.zap.zombiesplugin.pathfinder.PathfinderGoalMeleeAttackUnlimited;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MobBasicZombie extends EntityZombie {
    public MobBasicZombie(Location location) {
        super(((CraftWorld)location.getWorld()).getHandle());
        this.setPosition(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public boolean a(EntityHuman entity, EnumHand enumhand) {
        return super.a(entity, enumhand);
    }

    @Override
    public void initPathfinder() {
        this.goalSelector.a(1, new PathfinderGoalEscapeWindow(this, 1D, new Vector(0, 0, 0)));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttackUnlimited(this, 1.0D, false));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
    }
}
