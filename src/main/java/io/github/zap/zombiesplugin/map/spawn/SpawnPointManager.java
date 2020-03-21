package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.mob.Mob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;

public abstract class SpawnPointManager {

	// TODO: Resolve conflicts between different SpawnPointManager accepted mob types?

	/**
	 * The list of possible mob types the spawn points can spawn
	 */
	private final Class<? extends Mob>[] acceptedMobTypes;

	/**
	 * The list of spawnpoints the manager manages
	 */
	private final SpawnPoint[] spawnPoints;

	/** Creates the spawn point manager
	 *
	 * @param acceptedMobTypes The list of possible mob types the spawn points can spawn
	 * @param coordinates The list of spawn points the manager should manage
	 */
	public SpawnPointManager(GameManager gameManager, Class<? extends Mob>[] acceptedMobTypes, Location[] coordinates) {
		this.acceptedMobTypes = acceptedMobTypes;

		spawnPoints = new SpawnPoint[coordinates.length];

		for (int i = 0; i < spawnPoints.length; i++) {
			spawnPoints[i] = new SpawnPoint(coordinates[i]);
		}

	}

	/**
	 * Spawns mobs according to the allowed type
	 */
	public void spawn(List<Mob> mobs) {
		List<SpawnPoint> availableSpawnPoints = new ArrayList<>();
		List<Mob> acceptedMobs = new ArrayList<>();

		for (SpawnPoint spawnPoint : spawnPoints) {
			if (spawnPoint.isAvailable()) {
				availableSpawnPoints.add(spawnPoint);
			}
		}

		for (Mob mob : mobs) {
			if (Arrays.stream(acceptedMobTypes).anyMatch(mobClass -> mobClass.isInstance(mob))) {
				acceptedMobs.add(mob);
				mobs.remove(mob);
			}
		}

		Collections.shuffle(availableSpawnPoints);

		for (int i = 0; i < acceptedMobs.size(); i++) {
			availableSpawnPoints.get(i % availableSpawnPoints.size()).spawn(acceptedMobs.get(i));
		}
	}

	/** Gets a list of available spawn points which can currently be used
	 *
	 * @return The list of spawn points
	 */
	public List<SpawnPoint> getAvailableSpawnPoints() {
		List<SpawnPoint> availableSpawnPoints = new ArrayList<>();

		for (SpawnPoint spawnpoint : spawnPoints) {
			if (spawnpoint.isAvailable()) {
				availableSpawnPoints.add(spawnpoint);
			}
		}

		return availableSpawnPoints;
	}

	/** Gets the list of accepted mob types the spawn point manager can spawn
	 *
	 * @return The list of accepted mob types
	 */
	public Class<? extends Mob>[] getAcceptedMobTypes() {
		return acceptedMobTypes;
	}
}