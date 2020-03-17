package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.mob.Mob;
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

	/** Initializes the spawnpoint with the coordinates of the spawnpoint
	 *
	 * @param coordinates The spawnpoint coordinates
	 */
	public SpawnPoint(Location coordinates) {
		this.coordinates = coordinates;
	}

	/** Spawns mobs in the spawnpoint
	 *
	 * @param mob The mobs to spawn
	 */
	public void spawn(Mob mob) {
		// TODO: Actually spawning the mob
	}

	/** Tests whether or not the spawn point can be used
	 *
	 * @return The availability of the spawn point
	 */
	public boolean isAvailable() {
		return available;
	}

	/** Makes the spawn point usable
	 *
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
