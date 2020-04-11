package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.ISpawnpointContainer;
import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.map.Window;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitWorld;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.Location;

import java.util.ArrayList;

public class SpawnPoint {
	private final ISpawnpointContainer container;
	private final Location spawn;
	private final Location target;

	public SpawnPoint(Location spawn, Location target, ISpawnpointContainer container)
	{
		this.container = container;
		this.spawn = spawn;
		this.target = target;
	}

	public void spawn(GameManager manager, MythicMob mob) {
		BukkitWorld world = new BukkitWorld(spawn.getWorld());
		int difficulty = manager.getSettings().getDifficulty().ordinal();

		AbstractEntity entity = mob.spawn(new AbstractLocation(world, spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ()), difficulty).getEntity();
		entity.setMetadata("zp_manager", manager);
		entity.setMetadata("zp_spawnpoint", this);
	}

	public boolean canSpawn() {
		return container.canSpawn();
	}

	public Location getSpawn() {
		return spawn;
	}

	public Location getTarget() {
		return target;
	}

	public ISpawnpointContainer getContainer() {
		return container;
	}
}
