package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.map.data.SpawnPointData;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitWorld;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.Location;

public class SpawnPoint {
	private final Location spawn;
	private final Location target;
	private final Room room;

	public SpawnPoint(Room room, Location spawnCoordinates, Location target)
	{
		this.spawn = spawnCoordinates;
		this.target = target;
		this.room = room;
	}

	public void spawn(GameManager manager, MythicMob mob) {
		ZombiesPlugin.instance.lastSpawnpoint = this;
		ZombiesPlugin.instance.lastManager = manager;

		mob.spawn(new AbstractLocation(new BukkitWorld(spawn.getWorld()), spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ()), 0);

		ZombiesPlugin.instance.lastSpawnpoint = null;
		ZombiesPlugin.instance.lastManager = null;
	}

	public boolean canSpawn() {
		return true;
	}

	public Location getSpawn() {
		return spawn;
	}

	public Location getTarget() { return target; }

	public Room getRoom() { return room; }

	@Override
	public String toString() {
		return "Spawn: " + spawn.toString() + " Target: " + target.toString();
	}
}
