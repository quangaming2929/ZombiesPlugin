package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.map.spawn.SpawnPointManager;
import io.github.zap.zombiesplugin.round.Round;

public class Map {
	private SpawnPointManager[] spawnPointManagers;

	/**
	 * The rounds in the game
	 */
	private final Round[] rounds;

	public Map(SpawnPointManager[] spawnPointManagers, Round[] rounds) {
		this.spawnPointManagers = spawnPointManagers;
		this.rounds = rounds;
	}

	/** Gets the rounds for the map
	 *
	 * @return The rounds
	 */
	public Round[] getRounds() {
		return rounds;
	}

	/** Gets the spawn point managers
	 *
	 * @return The spawn point managers
	 */
	public SpawnPointManager[] getSpawnPointManagers() {
		return spawnPointManagers;
	}
}
