package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitWorld;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import org.bukkit.Location;
import java.util.HashMap;
import java.util.UUID;

public class GlobalMobManager {
    private HashMap<UUID, SpawnPoint> mobs;

    public GlobalMobManager() {
        mobs = new HashMap<>();
    }

    public void spawn(SpawnPoint point, MythicMob mob) {
        Location spawnLocation = point.getSpawn();
        ActiveMob active = mob.spawn(new AbstractLocation(new BukkitWorld(spawnLocation.getWorld()), spawnLocation.getY(), spawnLocation.getX(), spawnLocation.getZ()), 0);
        mobs.put(active.getUniqueId(), point);
    }

    public SpawnPoint getSpawnPoint(ActiveMob mob) {
        return mobs.get(mob.getUniqueId());
    }
}
