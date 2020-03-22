package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.mob.Mob;

import java.util.*;

import io.github.zap.zombiesplugin.mob.MobInfo;
import org.bukkit.Location;

public abstract class SpawnPointManager {
	protected final GameManager manager;
	/**
	 * The list of possible mob types the spawn points can spawn
	 */
	protected final HashSet<MobInfo> acceptedMobTypes;

	/**
	 * The list of spawnpoints the manager manages
	 */
	protected final SpawnPoint[] spawnPoints;

	/** Creates the spawn point manager
	 *
	 * @param acceptedMobTypes The list of possible mob types the spawn points can spawn
	 * @param coordinates The list of spawn points the manager should manage
	 */
	public SpawnPointManager(GameManager gameManager, HashSet<MobInfo> acceptedMobTypes, Location[] coordinates) {
		this.acceptedMobTypes = acceptedMobTypes;
		this.manager = gameManager;
		spawnPoints = new SpawnPoint[coordinates.length];

		for (int i = 0; i < spawnPoints.length; i++) {
			spawnPoints[i] = new SpawnPoint(coordinates[i], gameManager);
		}
	}

	/**
	 * Spawns mobs according to the allowed type
	 */
	public abstract void spawn(ArrayList<Mob> mobs);

	public abstract boolean isValid();
}
