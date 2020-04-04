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
	private final ArrayList<Room> rooms;

	public GameMap(String name) {
		this.name = name;
		this.spawnFilters = new ArrayList<>();
		this.rounds = new ArrayList<>();
		this.rooms = new ArrayList<>();

		//TODO: load from config file
	}

	public Window getAvailableWindow(Location location) {
		for(Room room : rooms) {
			if(room.isOpen()) {
				for(Window window : room.getWindows()) {
					if(window.getWindowBounds().isInBound(location)) return window;
				}
			}
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
		rooms.add(room);
	}

	public String getName() { return name; }

	public ArrayList<SpawnFilter> getSpawnFilters() {
		return spawnFilters;
	}

	public ArrayList<Round> getRounds() {
		return rounds;
	}

	public ArrayList<Room> getRooms() { return rooms; }
}
