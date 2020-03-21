package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.map.round.Round;
import io.github.zap.zombiesplugin.map.spawn.SpawnPointManager;
import io.github.zap.zombiesplugin.shop.Shop;

public class Map {
	private SpawnPointManager[] spawnPointManagers;

	/**
	 * The rounds in the game
	 */
	private final Round[] rounds;

	private final Shop[] shops;

	public Map(SpawnPointManager[] spawnPointManagers, Round[] rounds, Shop[] shops) {
		this.spawnPointManagers = spawnPointManagers;
		this.rounds = rounds;
		this.shops = shops;
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

	public Shop[] getShops() {
		return shops;
	}
}