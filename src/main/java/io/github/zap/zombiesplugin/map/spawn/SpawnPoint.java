package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.Room;
import io.lumine.xikage.mythicmobs.MythicMobs;
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
		this.room = room;
		this.spawn = spawnCoordinates;
		this.target = target;
	}

	public void spawn(MythicMob mob) {
		ZombiesPlugin.globalMobManager.spawn(this, mob);
	}

	public GameManager getManager() { return manager; }
	public boolean canSpawn() {
		return room.isOpen();
	}

	public Location getSpawn() {
		return spawn;
	}

	public Location getTarget() { return target; }

	public Room getRoom() { return room; }
}
