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

import java.util.Optional;

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
    private boolean loadedMetadata = false;

    private final int searchDistance = 16;
    private final double testDistance = 1;
    private final int radiusSquared = 4;

    private int tickCounter = 0;

    public PathfinderGoalEscapeWindow(AbstractEntity entity, String line, MythicLineConfig mlc) {
        super(entity, line, mlc);
        setGoalType(GoalType.MOVE_LOOK);

        nmsEntity = (EntityCreature)((CraftEntity)entity.getBukkitEntity()).getHandle();
    }

    private void loadMetadata() {
        if(entity.hasMetadata("zp_manager") && entity.hasMetadata("zp_spawnpoint")) {
            Optional<Object> optManager = entity.getMetadata("zp_manager");
            Optional<Object> optSpawnpoint = entity.getMetadata("zp_spawnpoint");

            if(optManager.isPresent() && optSpawnpoint.isPresent()) {
                manager = (GameManager)optManager.get();
                SpawnPoint spawnPoint = (SpawnPoint)optSpawnpoint.get();

                Location target = spawnPoint.getTarget();
                if(target != null) {
                    destination = new AbstractLocation(entity.getWorld(), target.getBlockX(), target.getBlockY(), target.getBlockZ());
                    hasWindow = true;
                }

                loadedMetadata = true;
            }
        }
    }

    public boolean shouldStart() {
        if(!loadedMetadata) {
            loadMetadata();
            return false;
        }
        else return this.entity.getLocation().distanceSquared(destination) > (double)radiusSquared && !reachedGoal;
    }

    @Override
    public void start() {
        if(destination != null) {
            ai().navigateToLocation(this.entity, destination, searchDistance);
        }
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
        targetWindow = null;
        reachedGoal = true;
    }

    public void tryBreak() {
        World world = ((BukkitWorld) entity.getWorld()).getBukkitWorld();
        AbstractLocation loc = entity.getLocation();

        double posX = loc.getX();
        double posY = loc.getY();
        double posZ = loc.getZ();

        float yaw = loc.getYaw();

        Location testLoc;
        if (yaw < 0) {
            yaw += 360;
        }
        if (yaw >= 315 || yaw < 45) {
            testLoc = new Location(world, posX, posY + 1, posZ + testDistance);
        } else if (yaw < 135) {
            testLoc = new Location(world, posX - testDistance, posY + 1, posZ);
        } else if (yaw < 225) {
            testLoc = new Location(world, posX, posY + 1, posZ - testDistance);
        } else {
            testLoc = new Location(world, posX + testDistance, posY + 1, posZ);
        }

        Window foundWindow = manager.getSettings().getGameMap().getAvailableWindow(testLoc);
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
