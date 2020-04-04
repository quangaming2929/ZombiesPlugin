package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.map.round.Round;
import io.github.zap.zombiesplugin.map.spawn.SpawnFilter;
import io.github.zap.zombiesplugin.shop.Shop;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class GameMap {
	private final String name;
	private final ArrayList<SpawnFilter> spawnFilters;
	private final ArrayList<Round> rounds;
	private final HashMap<String,Room> rooms;

	public GameMap(String name) {
		this.name = name;
		this.spawnFilters = new ArrayList<>();
		this.rounds = new ArrayList<>();
		this.rooms = new HashMap<>();

		//TODO: load from config file
	}

	public Window getAvailableWindow(Location location) {
		System.out.println("getAvailableWindow called, there are " + rooms.size() + " rooms.");
		for(Room room : rooms.values()) {
			if(room.isOpen()) {
				System.out.println("room is open, it has "+room.getWindows().size() + " windows");
				Window sample = room.getWindowAt(location);
				if(sample != null) {
					System.out.println("returning sample");
					return sample;
				}
				else System.out.println("returning null");
			}
			else System.out.println("room is not open");
		}
		return null;
	}

	public void add(SpawnFilter manager) {
		spawnFilters.add(manager);
	}

	public void add(Round round) {
		rounds.add(round);
	}

	public void add(Room room) {
		rooms.put(room.getName(), room);
	}

	public String getName() { return name; }

	public ArrayList<SpawnFilter> getSpawnFilters() {
		return spawnFilters;
	}

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
}
