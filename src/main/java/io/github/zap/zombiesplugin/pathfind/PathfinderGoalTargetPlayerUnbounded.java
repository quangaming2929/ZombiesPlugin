package io.github.zap.zombiesplugin.pathfind;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitWorld;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ai.Pathfinder;
import io.lumine.xikage.mythicmobs.mobs.ai.PathfindingGoal;
import io.lumine.xikage.mythicmobs.util.annotations.MythicAIGoal;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

@MythicAIGoal(
        name = "unboundedPlayerTarget",
        description = "Special goal that only cares about players in the current game and has an infinite range."
)
public class PathfinderGoalTargetPlayerUnbounded extends Pathfinder implements PathfindingGoal {
    private GameManager game;
    private User target;

    public PathfinderGoalTargetPlayerUnbounded(AbstractEntity entity, String line, MythicLineConfig mlc) {
        super(entity, line, mlc);
        game = ZombiesPlugin.instance.lastSpawnpoint.getGameManager();
    }

    @Override
    public boolean shouldStart() {
        return game != null && !game.hasEnded() && game.getPlayerManager().getPlayers().size() > 0;
    }

    @Override
    public void start() {
        nearestPlayer();
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean shouldEnd() {
        return game == null || (target != null || game.hasEnded() || game.getPlayerManager().getPlayers().size() == 0);
    }

    @Override
    public void end() {

    }

    private void nearestPlayer() {
        double lowest = Double.MAX_VALUE;
        User closest = null;
        for(User user : game.getPlayerManager().getPlayers()) {
            Location loc = user.getPlayer().getLocation();

            double dyst = this.entity.getLocation().distanceSquared(new AbstractLocation(new BukkitWorld(loc.getWorld()), loc.getX(), loc.getY(), loc.getZ()));
            if(dyst < lowest) {
                lowest = dyst;
                closest = user;
            }
        }

        target = closest;
        ai().setTarget((LivingEntity)this.entity.getBukkitEntity(), target.getPlayer());
    }
}
