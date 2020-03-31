package io.github.zap.zombiesplugin.pathfind;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.map.Window;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.github.zap.zombiesplugin.memes.Direction;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitWorld;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ai.Pathfinder;
import io.lumine.xikage.mythicmobs.mobs.ai.PathfindingGoal;
import io.lumine.xikage.mythicmobs.util.annotations.MythicAIGoal;
import net.minecraft.server.v1_15_R1.EntityCreature;
import net.minecraft.server.v1_15_R1.Vec3D;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.util.Vector;

@MythicAIGoal(
        name = "escapeWindow",
        description = "Used by zombies to navigate out of windows."
)
public class PathfinderGoalEscapeWindow extends Pathfinder implements PathfindingGoal {
    private EntityCreature nmsEntity;
    private SpawnPoint spawnPoint;
    private Window targetWindow = null;
    private AbstractLocation destination;
    private boolean reachedGoal = false;
    private boolean hasWindow = false;

    private final int searchDistance = 20;
    private final int radiusSquared = 2;

    private int tickCounter = 0;

    public PathfinderGoalEscapeWindow(AbstractEntity entity, String line, MythicLineConfig mlc) {
        super(entity, line, mlc);
        setGoalType(GoalType.MOVE_LOOK);

        spawnPoint = ZombiesPlugin.instance.lastSpawnpoint;
        if(spawnPoint != null) {
            Location testSpawnLocation = spawnPoint.getSpawn();
            AbstractLocation location = entity.getLocation();
            if(testSpawnLocation.getBlockX() == location.getBlockX() &&
                    testSpawnLocation.getBlockY() == location.getBlockY() &&
                    testSpawnLocation.getBlockZ() == location.getBlockZ()) {

                Location target = spawnPoint.getTarget();
                destination = new AbstractLocation(entity.getWorld(), target.getBlockX(), target.getBlockY(), target.getBlockZ());
                hasWindow = true;
            }
        }

        nmsEntity = (EntityCreature)((CraftEntity)entity.getBukkitEntity()).getHandle();
    }

    public boolean shouldStart() {
        return this.entity.getLocation().distanceSquared(destination) > (double)radiusSquared && !reachedGoal;
    }

    public void start() {
        ai().navigateToLocation(this.entity, destination, searchDistance);
    }

    public boolean canBreak() {
        return tickCounter % 25 == 0;
    }

    public void tick() {
        tickCounter++;
        if(canBreak()) {
            tryBreak();
            tickCounter = 0;
            nmsEntity.getControllerLook().a(new Vec3D(destination.getX(), destination.getY(), destination.getZ()));
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

    public void tryBreak() {
        World world = ((BukkitWorld) entity.getWorld()).getBukkitWorld();
        AbstractLocation loc = entity.getLocation();
        int posX = loc.getBlockX();
        int posY = loc.getBlockY();
        int posZ = loc.getBlockZ();

        Direction direction;
        float yaw = loc.getYaw();
        if(yaw <= -135 || yaw > 135) { //north
            direction = Direction.NORTH;
        }
        else if( yaw <= -45) {
            direction = Direction.EAST;
        }
        else if(yaw <= 45) {
            direction = Direction.SOUTH;
        }
        else {
            direction = Direction.WEST;
        }

        Vector testBlock;
        switch (direction) {
            case NORTH:
                testBlock = new Vector(posX, posY + 1, posZ - 0.5);
                break;
            case EAST:
                testBlock = new Vector(posX + 0.5, posY + 1, posZ);
                break;
            case SOUTH:
                testBlock = new Vector(posX, posY + 1, posZ + 0.5);
                break;
            case WEST:
                testBlock = new Vector(posX - 0.5, posY + 1, posZ);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }

        Location testLoc = new Location(world, testBlock.getBlockX(), testBlock.getBlockY(), testBlock.getBlockZ());
        Window foundWindow = spawnPoint.getGameManager().getMap().getWindowAt(testLoc);

        if(foundWindow == null && targetWindow != null) {
            targetWindow.setBreaking(false);
            targetWindow = null;
        }
        else if(foundWindow != null) {
            targetWindow = foundWindow;
            foundWindow.setBreaking(true);
            foundWindow.breakWindow();
        }
    }
}
