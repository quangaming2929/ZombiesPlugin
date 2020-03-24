package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.mob.CustomMob;

import io.github.zap.zombiesplugin.mob.MobInfo;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class SpawnPointManager {
	/**
	 * The list of possible mob types the spawn points can spawn
	 */
	protected final HashSet<MobInfo> acceptedMobTypes;

	/**
	 * The list of spawnpoints the manager manages
	 */
	protected final ArrayList<SpawnPoint> spawnPoints;

	protected final GameManager manager;

	/** Creates the spawn point manager
	 *
	 * @param acceptedMobTypes The list of possible mob types the spawn points can spawn
	 */
	public SpawnPointManager(GameManager manager, HashSet<MobInfo> acceptedMobTypes) {
		this.acceptedMobTypes = acceptedMobTypes;
		spawnPoints = new ArrayList<>();
		this.manager = manager;
	}

	/**
	 * Spawns mobs according to the allowed type
	 */
	public abstract void spawn(ArrayList<CustomMob> mobs);

	public abstract boolean isValid();

	public void add(SpawnPoint spawnPoint) {
		spawnPoints.add(spawnPoint);
	}

	public void add(CustomMob mob) {
		acceptedMobTypes.add(mob.getMobInfo());
	}
}
