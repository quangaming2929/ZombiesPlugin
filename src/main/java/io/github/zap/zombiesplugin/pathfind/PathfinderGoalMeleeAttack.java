package io.github.zap.zombiesplugin.pathfind;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ai.Pathfinder;
import io.lumine.xikage.mythicmobs.mobs.ai.PathfindingGoal;
import io.lumine.xikage.mythicmobs.util.annotations.MythicAIGoal;

@MythicAIGoal(
        name = "unboundedMeleeAttack",
        aliases = {"unboundedMeleeAttack"},
        description = "Used by zombies to target players after having left the window."
)
public class PathfinderGoalMeleeAttack extends Pathfinder implements PathfindingGoal {
    public PathfinderGoalMeleeAttack(AbstractEntity entity, String line, MythicLineConfig mlc) {
        super(entity, line, mlc);
    }

    @Override
    public boolean shouldStart() {
        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public void tick() {

    }

    @Override
    public boolean shouldEnd() {
        return false;
    }

    @Override
    public void end() {

    }
}
