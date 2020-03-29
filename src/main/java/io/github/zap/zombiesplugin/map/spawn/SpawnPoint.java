package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.Room;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitWorld;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.Location;

public class SpawnPoint {
	private final GameManager manager;
	private final Location spawn;
	private final Location target;
	private final Room room;

	public SpawnPoint(GameManager manager, Room room, Location spawnCoordinates, Location target)
	{
		this.manager = manager;
		this.spawn = spawnCoordinates;
		this.target = target;
		this.room = room;
	}

	public void spawn(MythicMob mob) {
		ZombiesPlugin.instance.lastSpawnpoint = this;
		System.out.println("spawn() called on SpawnPoint " + toString());
		mob.spawn(new AbstractLocation(new BukkitWorld(spawn.getWorld()), spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ()), 0);
		ZombiesPlugin.instance.lastSpawnpoint = null;
	}

	public GameManager getManager() { return manager; }

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
		return "Manager: " + manager.toString() + " Spawn: " + spawn.toString() + " Target: " + target.toString();
	}
}
