package io.github.zap.zombiesplugin.pathfind;

import io.github.zap.zombiesplugin.pathfind.nms.UnboundedMeleeAttack;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ai.WrappedPathfindingGoal;
import io.lumine.xikage.mythicmobs.util.annotations.MythicAIGoal;
import io.lumine.xikage.mythicmobs.volatilecode.v1_15_R1.ai.PathfinderHolder;
import net.minecraft.server.v1_15_R1.EntityCreature;
import net.minecraft.server.v1_15_R1.PathfinderGoal;

@MythicAIGoal(
        name = "unboundedMeleeAttack",
        description = "Used by zombies to target players after having left the window."
)
public class PathfinderGoalUnboundedMeleeAttack extends WrappedPathfindingGoal implements PathfinderHolder {
    public PathfinderGoalUnboundedMeleeAttack(AbstractEntity entity, String line, MythicLineConfig mlc) {
        super(entity, line, mlc);
    }

    @Override
    public PathfinderGoal create() {
        return new UnboundedMeleeAttack((EntityCreature)PathfinderHolder.getNMSEntity(this.entity), 1.0D, true);
    }

    @Override
    public boolean isValid() {
        return this.entity.isCreature();
    }
}
