package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitWorld;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class GlobalMobManager implements Listener {
    private HashMap<UUID, SpawnPoint> mobs;

    public GlobalMobManager() {
        mobs = new HashMap<>();
        ZombiesPlugin.instance.getServer().getPluginManager().registerEvents(this, ZombiesPlugin.instance);
    }

    public void spawn(SpawnPoint point, MythicMob mob) {
        Location spawnLocation = point.getSpawn();
        ActiveMob active = mob.spawn(new AbstractLocation(new BukkitWorld(spawnLocation.getWorld()), spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ()), 0);
        mobs.put(active.getEntity().getUniqueId(), point);
    }

    public SpawnPoint getSpawnPoint(AbstractEntity mob) {
        if(mobs.containsKey(mob.getUniqueId())) {
            return mobs.get(mob.getUniqueId());
        }
        return null;
    }

    @EventHandler
    public void onMythicMobDie(MythicMobDeathEvent event) {
        UUID uuid = event.getMob().getUniqueId();
        if(mobs.containsKey(uuid)) {
            mobs.remove(uuid);
        }
    }
}
