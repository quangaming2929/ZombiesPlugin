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
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

@MythicAIGoal(
        name = "unboundedPlayerTarget",
        description = "Special goal that only cares about players in the current game and has an infinite range."
)
public class PathfinderGoalTargetPlayerUnbounded extends Pathfinder implements PathfindingGoal {
    private int tickCount;
    private final GameManager game;
    private User target;

    public PathfinderGoalTargetPlayerUnbounded(AbstractEntity entity, String line, MythicLineConfig mlc) {
        super(entity, line, mlc);
        game = ZombiesPlugin.instance.lastManager;
    }

    @Override
    public boolean shouldStart() {
        return game != null && !game.hasEnded() && game.getUserManager().getPlayers().size() > 0;
    }

    @Override
    public void start() {
        targetNearestUser();
    }

    /**
     * Stop targeting if the player isn't in survival or adventure mode. If we haven't already targeted a player,
     * attempt to retarget every second. The latter should not happen during an actual game.
     */
    @Override
    public void tick() {
        if(target != null) {
            GameMode mode = target.getPlayer().getGameMode();
            //TODO: check if target is downed
            if(mode == GameMode.CREATIVE || mode == GameMode.SPECTATOR || target.getPlayer().isDead()) {
                target = null;
                ai().setTarget((LivingEntity)this.entity.getBukkitEntity(), null);
            }
        }
        else {
            tickCount++;
            if(tickCount == 10) {
                targetNearestUser();
                tickCount = 0;
            }
        }
    }

    @Override
    public boolean shouldEnd() {
        return game == null || game.hasEnded();
    }

    @Override
    public void end() { }

    /**
     * Sets the closest valid player as this entity's target. Cannot target players who are not participating in a game,
     * or who are not in survival or adventure mode.
     */
    private void targetNearestUser() {
        double shortest = Double.MAX_VALUE;
        User closest = null;

        //finds the closest User to target
        for(User user : game.getUserManager().getPlayers()) {
            GameMode mode = user.getPlayer().getGameMode();
            if(mode != GameMode.SPECTATOR && mode != GameMode.CREATIVE) {
                Location loc = user.getPlayer().getLocation();

                double dist = this.entity.getLocation().distanceSquared(new AbstractLocation(new BukkitWorld(loc.getWorld()), loc.getX(), loc.getY(), loc.getZ()));
                if(dist < shortest) {
                    shortest = dist;
                    closest = user;
                }
            }
        }

        if(closest != null) {
            target = closest;
            ai().setTarget((LivingEntity)this.entity.getBukkitEntity(), target.getPlayer());
        }
    }
}
