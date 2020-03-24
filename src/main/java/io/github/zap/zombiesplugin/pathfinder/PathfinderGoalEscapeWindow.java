package io.github.zap.zombiesplugin.pathfinder;

import net.minecraft.server.v1_15_R1.*;
import org.bukkit.util.Vector;

public class PathfinderGoalEscapeWindow extends PathfinderGoalGotoTarget {
    private boolean escaped;

    public PathfinderGoalEscapeWindow(EntityCreature pathfindingEntity, double speed, Vector targetLocation) {
        super(pathfindingEntity, speed, 64); //var3 is pathfinding range
        super.e = new BlockPosition(targetLocation.getBlockX(), targetLocation.getBlockY(), targetLocation.getBlockZ());
    }

    @Override
    protected boolean a(IWorldReader iWorldReader, BlockPosition blockPosition) {
        return blockPosition.equals(e) && !escaped;
    }

    @Override
    public boolean a() {
        return !escaped;
    }

    @Override
    public boolean j() {
        return this.d % 20 == 0;
    }

    /*
     * this.e = target block vector
     * this.a = entity
     * this.h() = unknown
     * this.d = tick counter
     */
    @Override
    public void e() {
        if(!escaped) {
            if (!this.e.up().a(this.a.getPositionVector(), 1)) {
                ++this.d;
                if (this.j()) {
                    this.a.getNavigation().a((double)((float)this.e.getX()) + 0.5D, (this.e.getY() - 1), (double)((float)this.e.getZ()) + 0.5D, this.b);
                }
            } else {
                --this.d;
                escaped = true;
            }
        }
    }
}
