package io.github.zap.zombiesplugin.pathfinder;

import net.minecraft.server.v1_15_R1.*;
import org.bukkit.event.entity.EntityTargetEvent;

public class PathfinderGoalMeleeAttackUnlimited extends PathfinderGoalMeleeAttack {

    public PathfinderGoalMeleeAttackUnlimited(EntityCreature var0, double var1, boolean var3) {
        super(var0, var1, var3);
    }

    @Override
    public boolean a() {
        EntityLiving var2 = this.a.getGoalTarget();

        //TODO: Force retarget on target death
        if (var2 == null) {
            return false;
        }

        return true;
    }
}
