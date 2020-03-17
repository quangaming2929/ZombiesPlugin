package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.mob.Mob;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;

public class RangedSpawnPointManager extends SpawnPointManager {

	/**
	 * The Game manager
	 */
	private final GameManager gameManager;

	/**
	 * The maximum range from a player the spawn point manager spawns mobs from
	 */
	private final int spawnRange;

	/**
	 * Creates the spawn point manager
	 *
	 * @param acceptedMobTypes The list of possible mob types the spawn points can spawn
	 * @param coordinates      The list of spawn points the manager should manage
	 */
	public RangedSpawnPointManager(GameManager gameManager, Class<? extends Mob>[] acceptedMobTypes, Location[] coordinates, int spawnRange) {
		super(gameManager, acceptedMobTypes, coordinates);
		this.gameManager = gameManager;
		this.spawnRange = spawnRange;
	}

	@Override
	public List<SpawnPoint> getAvailableSpawnPoints() {
		List<SpawnPoint> availableSpawnPoints = super.getAvailableSpawnPoints();

		availableSpawnPoints.removeIf(spawnPoint -> Arrays.stream(gameManager.getPlayerManager().getActivePlayers()).anyMatch(player -> player.getLocation().distance(spawnPoint.getCoordinates()) <= spawnRange));

		return availableSpawnPoints;
	}
}
