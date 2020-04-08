package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.data.IProvideDescription;
import io.github.zap.zombiesplugin.manager.GameDifficulty;
import io.github.zap.zombiesplugin.map.round.Round;
import io.github.zap.zombiesplugin.map.spawn.SpawnFilter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameMap implements IProvideDescription {
	private final LookupHelper lookup;

	// reflect the
	private final String name;
	// Implement IProvideDescription
	private final List<String> description;
	private final Material mapIcon;
	private final List<GameDifficulty> acceptedDifficulty;

	private final ArrayList<SpawnFilter> spawnFilters;
	private final ArrayList<Round> rounds;
	private final HashMap<String,Room> rooms;

	// TODO: @John-DND: Request review, I don't know how you serialize the map config
	public GameMap(String name, List<String> description, Material mapIcon, List<GameDifficulty> acceptedDiffs) {
		lookup = new LookupHelper();
		this.name = name;
		this.spawnFilters = new ArrayList<>();
		this.rounds = new ArrayList<>();
		this.rooms = new HashMap<>();

		this.description = description;
		this.mapIcon = mapIcon;
		this.acceptedDifficulty = acceptedDiffs;

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

	public List<String> getDescription() {
		return description;
	}

	public Material getMapIcon() {
		return mapIcon;
	}

	public List<GameDifficulty> getAcceptedDifficulty() {
		return acceptedDifficulty;
	}

	@Override
	public ItemStack getDescriptionVisual() {
		ItemStack i = new ItemStack(getMapIcon(), 1);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(getName());
		meta.setLore(getDescription());
		i.setItemMeta(meta);

		return i;
	}


}
