package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.round.Round;
import io.github.zap.zombiesplugin.map.spawn.RandomizedSpawnPointManager;
import io.github.zap.zombiesplugin.map.spawn.SpawnPointManager;
import io.github.zap.zombiesplugin.shop.Shop;

import java.util.ArrayList;

public class Map {
	private final GameManager manager;

	private ArrayList<SpawnPointManager> spawnPointManagers;

	/**
	 * The rounds in the game
	 */
	private final ArrayList<Round> rounds;

	private final ArrayList<Shop> shops;

	private final ArrayList<Door> doors;

	//default map, used for debugging
	public Map(GameManager manager) {
		this.manager = manager;
		this.spawnPointManagers = new ArrayList<>();
		this.rounds = new ArrayList<>();
		this.shops = new ArrayList<>();
		this.doors = new ArrayList<>();
	}

	/** Gets the rounds for the map
	 *
	 * @return The rounds
	 */
	public ArrayList<Round> getRounds() {
		return rounds;
	}

	/** Gets the spawn point managers
	 *
	 * @return The spawn point managers
	 */
	public ArrayList<SpawnPointManager> getSpawnPointManagers() {
		return spawnPointManagers;
	}

	public ArrayList<Shop> getShops() {
		return shops;
	}

	public void add(SpawnPointManager manager) {
		spawnPointManagers.add(manager);
	}

	public void add(Round round) {
		rounds.add(round);
	}

	public void add(Shop shop) {
		shops.add(shop);
	}

	public void add(Door door) {
		doors.add(door);
	}
}
