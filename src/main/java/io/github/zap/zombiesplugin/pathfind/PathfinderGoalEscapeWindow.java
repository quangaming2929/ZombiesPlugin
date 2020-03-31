package io.github.zap.zombiesplugin.pathfind;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ai.Pathfinder;
import io.lumine.xikage.mythicmobs.mobs.ai.PathfindingGoal;
import io.lumine.xikage.mythicmobs.util.annotations.MythicAIGoal;
import org.bukkit.Location;

@MythicAIGoal(
        name = "escapeWindow",
        description = "Used by zombies to navigate out of windows."
)
public class PathfinderGoalEscapeWindow extends Pathfinder implements PathfindingGoal {
    private AbstractLocation destination;
    private boolean reachedGoal = false;
    private boolean hasWindow = false;

    private final int searchDistance = 20;
    private final int radiusSquared = 4;

    private int tickCounter = 0;

    public PathfinderGoalEscapeWindow(AbstractEntity entity, String line, MythicLineConfig mlc) {
        super(entity, line, mlc);
        setGoalType(GoalType.MOVE);

        SpawnPoint spawnpoint = ZombiesPlugin.instance.lastSpawnpoint;
        if(spawnpoint != null) {
            Location testSpawnLocation = spawnpoint.getSpawn();
            AbstractLocation location = entity.getLocation();
            if(testSpawnLocation.getBlockX() == location.getBlockX() &&
                    testSpawnLocation.getBlockY() == location.getBlockY() &&
                    testSpawnLocation.getBlockZ() == location.getBlockZ()) {

                Location target = spawnpoint.getTarget();
                destination = new AbstractLocation(entity.getWorld(), target.getBlockX(), target.getBlockY(), target.getBlockZ());
                hasWindow = true;
            }
        }
    }

    public boolean shouldStart() {
        return this.entity.getLocation().distanceSquared(destination) > (double)radiusSquared && !reachedGoal;
    }

    public void start() {
        ai().navigateToLocation(this.entity, destination, searchDistance);
    }

    public boolean tryBreak() {
        return tickCounter % 20 == 0;
    }

    public void tick() {
        tickCounter++;
        if(tryBreak()) {

        }
        ai().navigateToLocation(this.entity, destination, searchDistance);
    }

    public boolean shouldEnd() {
        if(this.entity.getLocation().distanceSquared(destination) <= (double)radiusSquared || !hasWindow) {
            reachedGoal = true;
            return true;
        }
        return false;
    }

    @Override
    public void end() { }
}
