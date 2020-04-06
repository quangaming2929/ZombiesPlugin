package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.map.round.Round;
import io.github.zap.zombiesplugin.map.spawn.SpawnFilter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class GameMap {
	private final LookupHelper lookup;

	// reflect the
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

	public Window getAvailableWindow(Location location) {
		for(Room room : rooms.values()) {
			if(room.isOpen()) {
				Window sample = room.getWindowAt(location);
				if(sample != null) {
					return sample;
				}
			}
		}
		return null;
	}

	public void add(SpawnFilter manager) { spawnFilters.add(manager); }

	public void add(Round round) { rounds.add(round); }

	public void add(Room room) {
		room.setLookup(lookup);
		rooms.put(room.getName(), room);
	}

	public String getName() { return name; }

	public ArrayList<SpawnFilter> getSpawnFilters() { return spawnFilters; }

	public ArrayList<Round> getRounds() {
		return rounds;
	}

	public ArrayList<Room> getRooms() { return new ArrayList<>(rooms.values()); }

	public Room getRoom(String name) {
		if(rooms.containsKey(name)) {
			return rooms.get(name);
		}
		return null;
	}

	public LookupHelper getLookupHelper() { return lookup; }
}
