package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.Room;
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
		this.room = room;
		this.spawn = spawnCoordinates;
		this.target = target;
	}

	public void spawn(GameManager game, MythicMob mob) {
		mob.spawn(new AbstractLocation(new BukkitWorld(spawn.getWorld()), spawn.getX(), spawn.getY(), spawn.getZ()), 0);
	}

	public boolean canSpawn() {
		return room.isOpen();
	}

	public Location getSpawn() {
		return spawn;
	}

	public Location getTarget() { return target; }

	public Room getRoom() { return room; }
}
