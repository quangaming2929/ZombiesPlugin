package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.mob.CustomMob;
import org.bukkit.Location;

public class SpawnPoint {

	/**
	 * Whether or not the spawn point can be used
	 */
	private boolean available = false;

	/**
	 * The location of the actual spawn point
	 */
	private final Location coordinates;

	private final GameManager manager;

	/** Initializes the spawnpoint with the coordinates of the spawnpoint
	 *
	 * @param coordinates The spawnpoint coordinates
	 */
	public SpawnPoint(GameManager manager, Location coordinates)
	{
		this.manager = manager;
		this.coordinates = coordinates;
	}

	/** Spawns mobs in the spawnpoint
	 *
	 * @param mob The mobs to spawn
	 */
	public void spawn(CustomMob mob) {
		// TODO: Actually spawning the mob
	}

	/** Tests whether or not the spawn point can be used
	 *
	 * @return The availability of the spawn point
	 */

	//always true for debugging purposes
	public boolean isAvailable() {
		return true;
	}

	/**
	 * Makes the spawn point usable
	 */
	public void makeAvailable() {
		available = true;
	}

	/** Gets the coordinates of the spawnpoint
	 *
	 * @return The coordinates
	 */
	public Location getCoordinates() {
		return coordinates;
	}
}
