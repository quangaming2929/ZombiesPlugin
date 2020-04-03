package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.map.round.Round;
import io.github.zap.zombiesplugin.map.spawn.SpawnFilter;
import io.github.zap.zombiesplugin.shop.Shop;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class GameMap {
	public final String name;
	private final ArrayList<SpawnFilter> spawnFilters;
	private final ArrayList<Round> rounds;
	private final ArrayList<Shop> shops;
	private final ArrayList<Door> doors;
	private final HashMap<String,Room> rooms;
	private final ArrayList<Window> windows;
	private final ArrayList<BoundingBox> boundsLimits;

	public GameMap(String name) {
		this.name = name;
		this.rooms = new HashMap<>();
		this.spawnFilters = new ArrayList<>();
		this.rounds = new ArrayList<>();
		this.shops = new ArrayList<>();
		this.doors = new ArrayList<>();
		this.windows = new ArrayList<>();
		this.boundsLimits = new ArrayList<>();

		//TODO: load from config file
	}

	public ArrayList<Round> getRounds() {
		return rounds;
	}

	public ArrayList<SpawnFilter> getSpawnFilters() {
		return spawnFilters;
	}

	public ArrayList<Window> getWindows() {
		return windows;
	}

	public ArrayList<Shop> getShops() {
		return shops;
	}

	public Window getWindowAt(Location location) {
		for(Window window : windows) {
			if(window.getWindowBounds().isInBound(location)) {
				return window;
			}
		}
		return null;
	}

	public Room getRoom(String name) {
		if(rooms.containsKey(name)) {
			return rooms.get(name);
		}
		return null;
	}

	public ArrayList<Room> getRooms() {
		return new ArrayList<>(rooms.values());
	}

	public void add(SpawnFilter manager) {
		spawnFilters.add(manager);
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

	public void add(Room room) {
		rooms.put(room.getName(), room);
	}

	public void add(Window window) {
		windows.add(window);
	}

	public void add(BoundingBox limit) {
		boundsLimits.add(limit);
	}
}
