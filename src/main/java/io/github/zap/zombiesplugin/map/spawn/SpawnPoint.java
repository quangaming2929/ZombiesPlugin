package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.map.Window;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitWorld;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.Location;

public class SpawnPoint {
	private final Location spawn;
	private final Location target;

	public SpawnPoint(Location spawn, Location target)
	{
		this.spawn = spawn;
		this.target = target;
	}

	public void spawn(GameManager manager, MythicMob mob) {
		ZombiesPlugin.instance.lastSpawnpoint = this;
		ZombiesPlugin.instance.lastManager = manager;

		mob.spawn(new AbstractLocation(new BukkitWorld(spawn.getWorld()), spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ()), manager.getSettings().getDifficulty().ordinal());

		ZombiesPlugin.instance.lastSpawnpoint = null;
		ZombiesPlugin.instance.lastManager = null;
	}

	public Location getSpawn() {
		return spawn;
	}

	public Location getTarget() { return target; }
}
