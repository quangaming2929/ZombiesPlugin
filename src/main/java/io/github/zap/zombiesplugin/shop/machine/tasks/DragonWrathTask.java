package io.github.zap.zombiesplugin.shop.machine.tasks;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.data.TMTaskData;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.shop.machine.TeamMachineTask;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.swing.text.html.parser.Entity;
import java.util.Collection;

// TODO: Change Mob to some managed Enemies to avoid stuck unwanted Mob
public class DragonWrathTask extends TeamMachineTask {
    public static final String TMT_WRATH_RADIUS = "dwRadius";
    public static final String TMT_WRATH_DELAY = "dwDelay";

    public DragonWrathTask(TMTaskData data, GameManager manager) {
        super(data, manager);
    }

    @Override
    public boolean execTask(User user) {
        new BukkitRunnable() {
            @Override
            public void run() {
                World world = user.getPlayer().getWorld();

                for (User player : manager.getUserManager().getPlayers()) {
                    for (Mob mob : world.getNearbyEntitiesByType(Mob.class, player.getPlayer().getLocation(), getRadius())) {
                        world.strikeLightningEffect(mob.getLocation());
                        mob.setHealth(0);
                    }
                }
            }
        }.runTaskLater(ZombiesPlugin.instance, (int)(getDelay() * 20));


        return true;
    }

    private float getRadius() {
        return Float.parseFloat(tryGetCustomValue(TMT_WRATH_RADIUS));
    }

    private float getDelay() {
        return Float.parseFloat(tryGetCustomValue(TMT_WRATH_DELAY));
    }
}
