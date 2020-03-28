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
        aliases = {"escape"},
        description = "Used by zombies to navigate out of windows."
)
public class PathfinderGoalEscapeWindow extends Pathfinder implements PathfindingGoal {
    private AbstractLocation destination;
    private boolean reachedGoal = false;
    private boolean hasWindow = false;
    private long tickCount = 0;

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
        return this.entity.getLocation().distanceSquared(destination) > (double)1 && !reachedGoal;
    }

    public void start() {
        ai().navigateToLocation(this.entity, destination, 15);
    }

    public boolean shouldNavigate() {
        //returns true every 20 ticks (1 second)
        return tickCount % 20 == 0;
    }

    public void tick() {
        if(shouldNavigate()) {
            ai().navigateToLocation(this.entity, destination, 15);
        }
        else tickCount++;
    }

    public boolean shouldEnd() {
        if(this.entity.getLocation().distanceSquared(destination) <= (double)1 || !hasWindow) {
            reachedGoal = true;
            return true;
        }
        return false;
    }

    @Override
    public void end() { }
}
