package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.round.Round;
import io.github.zap.zombiesplugin.map.spawn.SpawnFilter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class GameMap {
	private final LookupHelper lookup;

	private final String name;
	private final ArrayList<SpawnFilter> spawnFilters;
	private final ArrayList<Round> rounds;
	private final HashMap<String,Room> rooms;

	public GameMap(String name) {
		lookup = new LookupHelper();
		this.name = name;
		this.spawnFilters = new ArrayList<>();
		this.rounds = new ArrayList<>();
		this.rooms = new HashMap<>();

		//TODO: load from config file
	}

	public void add(SpawnFilter manager) { spawnFilters.add(manager); }

	public void add(Round round) { rounds.add(round); }

	public void add(Room room) {
		rooms.put(room.getName(), room);
	}

	public String getName() { return name; }

	public ArrayList<SpawnFilter> getSpawnFilters() { return spawnFilters; }

	public ArrayList<Round> getRounds() {
		return rounds;
	}

	public ArrayList<Room> getRooms() { return new ArrayList<>(rooms.values()); }

	public boolean hasRoom(String name) { return rooms.containsKey(name); }

	public Room getRoom(String name) {
		if(rooms.containsKey(name)) {
			return rooms.get(name);
		}
		return null;
	}
}
