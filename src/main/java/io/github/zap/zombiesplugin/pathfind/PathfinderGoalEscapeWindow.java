package io.github.zap.zombiesplugin.pathfind;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
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
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

@MythicAIGoal(
        name = "escapeWindow",
        description = "Used by zombies to navigate out of windows."
)
public class PathfinderGoalEscapeWindow extends Pathfinder implements PathfindingGoal {
    private GameManager manager;
    private EntityCreature nmsEntity;
    private Window targetWindow = null;
    private AbstractLocation destination;
    private boolean reachedGoal = false;
    private boolean hasWindow = false;

    private final int searchDistance = 20;
    private final int radiusSquared = 3;

    private int tickCounter = 0;

    public PathfinderGoalEscapeWindow(AbstractEntity entity, String line, MythicLineConfig mlc) {
        super(entity, line, mlc);
        setGoalType(GoalType.MOVE_LOOK);

        SpawnPoint spawnPoint = ZombiesPlugin.instance.lastSpawnpoint;
        manager = ZombiesPlugin.instance.lastManager;
        nmsEntity = (EntityCreature)((CraftEntity)entity.getBukkitEntity()).getHandle();

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
    }

    public boolean shouldStart() {
        return this.entity.getLocation().distanceSquared(destination) > (double)radiusSquared && !reachedGoal;
    }

    @Override
    public void start() {
        ai().navigateToLocation(this.entity, destination, searchDistance);
    }

    @Override
    public void tick() {
        if(hasWindow) {
            tickCounter++;
            if(tickCounter == 20) {
                tryBreak();
                tickCounter = 0;
            }
        }

        nmsEntity.getControllerLook().a(new Vec3D(destination.getX(), destination.getY() + 1, destination.getZ()));
        ai().navigateToLocation(this.entity, destination, searchDistance);
    }

    @Override
    public boolean shouldEnd() {
        AbstractLocation entityLocation = this.entity.getLocation();
        return (entityLocation.distanceSquared(destination) <= (double) radiusSquared && entityLocation.getY() == destination.getY()) || !hasWindow;
    }

    @Override
    public void end() {
        //in most cases it is sufficient for tryBreak() to set window to null itself, but occasionally this may not work
        targetWindow = null;
        reachedGoal = true;
    }

    public void tryBreak() {
        World world = ((BukkitWorld) entity.getWorld()).getBukkitWorld();
        AbstractLocation loc = entity.getLocation();
        int posX = loc.getBlockX();
        int posY = loc.getBlockY();
        int posZ = loc.getBlockZ();

        //turn yaw into a direction so we know which way the zombie is facing
        Direction direction; //defaults to 'UP'
        float yaw = loc.getYaw();
        if(yaw <= -135 || yaw > 135) {
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

        Location testLoc;
        switch (direction) { //grab a vector in front of the zombie
            case NORTH:
                testLoc = new Location(world, posX, posY + 1, posZ - 0.5);
                break;
            case EAST:
                testLoc = new Location(world, posX + 0.5, posY + 1, posZ);
                break;
            case SOUTH:
                testLoc = new Location(world, posX, posY + 1, posZ + 0.5);
                break;
            case WEST:
                testLoc = new Location(world, posX - 0.5, posY + 1, posZ);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }

        Window foundWindow = manager.getMap().getWindowAt(testLoc);
        if(foundWindow == null && targetWindow != null) {
            targetWindow = null;
        }
        else if(foundWindow != null) {
            targetWindow = foundWindow;
            targetWindow.breakWindow( this);
        }
    }

    public boolean reachedGoal() { return reachedGoal || entity == null || entity.isDead(); }
}
