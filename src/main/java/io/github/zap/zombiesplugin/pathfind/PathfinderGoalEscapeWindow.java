package io.github.zap.zombiesplugin.pathfind;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitWorld;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ai.Pathfinder;
import io.lumine.xikage.mythicmobs.mobs.ai.PathfindingGoal;
import io.lumine.xikage.mythicmobs.util.annotations.MythicAIGoal;
import org.bukkit.Location;

@MythicAIGoal(
        name = "escapeWindow",
        aliases = {"escapeWindow"},
        description = "Used by zombies to navigate out of windows."
)
public class PathfinderGoalEscapeWindow extends Pathfinder implements PathfindingGoal {
    private AbstractLocation target;
    private boolean reachedGoal = false;
    private boolean hasWindow = false;

    public PathfinderGoalEscapeWindow(AbstractEntity entity, String line, MythicLineConfig mlc) {
        super(entity, line, mlc);

        SpawnPoint associatedPoint = ZombiesPlugin.instance.globalMobManager.getSpawnPoint(entity);
        if(associatedPoint != null) {
            System.out.println("associatedPoint is not null. Zombie will have behavior.");
            Location location = associatedPoint.getTarget();
            target = new AbstractLocation(new BukkitWorld(location.getWorld()), location.getBlockX(), location.getBlockY(), location.getBlockZ());
            hasWindow = true;
        }
    }

    @Override
    public boolean shouldStart() {
        return !reachedGoal && hasWindow;
    }

    @Override
    public void start() {
        ai().navigateToLocation(this.entity, target, 1F);
    }

    public void tick() {
        ai().navigateToLocation(this.entity, target, 1F);
        reachedGoal = entity.getLocation().getBlockX() == target.getBlockX() && entity.getLocation().getBlockY() == target.getBlockY() && entity.getLocation().getBlockZ() == target.getBlockZ();

        //TODO: break nearby windows
    }

    @Override
    public boolean shouldEnd() {
        return reachedGoal || !hasWindow;
    }

    @Override
    public void end() {

    }
}
