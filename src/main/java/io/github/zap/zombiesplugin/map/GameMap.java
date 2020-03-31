package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.round.Round;
import io.github.zap.zombiesplugin.map.spawn.SpawnManager;
import io.github.zap.zombiesplugin.shop.Shop;
import org.bukkit.Location;

import java.util.ArrayList;

public class GameMap {
	public final String name;
	private final ArrayList<SpawnManager> spawnManagers;
	private final ArrayList<Round> rounds;
	private final ArrayList<Shop> shops;
	private final ArrayList<Door> doors;
	private final ArrayList<Room> rooms;
	private final ArrayList<Window> windows;

	public GameMap(String name) {
		this.name = name;
		this.rooms = new ArrayList<>();
		this.spawnManagers = new ArrayList<>();
		this.rounds = new ArrayList<>();
		this.shops = new ArrayList<>();
		this.doors = new ArrayList<>();
		this.windows = new ArrayList<>();

		//TODO: load from config file
	}

	public ArrayList<Round> getRounds() {
		return rounds;
	}

	public ArrayList<SpawnManager> getSpawnManagers() {
		return spawnManagers;
	}

	public ArrayList<Window> getWindows() {
		return windows;
	}

	public ArrayList<Shop> getShops() {
		return shops;
	}

	public Window getWindowAt(Location location) {
		System.out.println("		Attempting to retrieve window. There is/are " + windows.size() + " currently active.");
		for(Window window : windows) {
			if(window.isInBound(location)) return window;
		}
		System.out.println("		No window found.");
		return null;
	}

	public void add(SpawnManager manager) {
		spawnManagers.add(manager);
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
		rooms.add(room);
	}

	public void add(Window window) {
		windows.add(window);
	}
}
