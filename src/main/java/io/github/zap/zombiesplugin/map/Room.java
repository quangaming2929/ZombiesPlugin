package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.github.zap.zombiesplugin.shop.Shop;
import org.bukkit.Location;

import java.util.ArrayList;

public class Room {
    private LookupHelper lookup;
    private final String name;
    private ArrayList<Door> doors;
    private ArrayList<ISpawnpointContainer> spawnPoints;
    private ArrayList<Shop> shops;
    private MultiBoundingBox bounds;
    private boolean open;

    public Room(String name, MultiBoundingBox bounds, LookupHelper helper) {
        lookup = helper;
        this.name = name;
        doors = new ArrayList<>();
        spawnPoints = new ArrayList<>();
        shops = new ArrayList<>();
        this.bounds = bounds;
    }

    public Room(String name, LookupHelper helper) {
        this(name, new MultiBoundingBox(), helper);
    }

    public void setLookup(LookupHelper helper) {
        if(lookup == null) lookup = helper;
    }

    public void add(Door door) { doors.add(door); }

    public void add(Window window) {
        lookup.addMapping(window, this);
        lookup.addMapping(window.getSpawnpoint(), window);
        spawnPoints.add(window);
    }

    public void add(SpawnPoint spawnPoint) {
        lookup.addMapping(spawnPoint, this);
        spawnPoints.add(spawnPoint);
    }

    public void add(Shop shop) { shops.add(shop); }

    public boolean isInBounds(Location location) {
        return bounds.isInBound(location);
    }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
    }

    public String getName() { return name; }

    public ArrayList<Door> getDoors() { return doors; }

    public ArrayList<ISpawnpointContainer> getSpawnPoints() { return spawnPoints; }

    public ArrayList<Shop> getShops() { return shops; }

    public MultiBoundingBox getBounds() { return bounds; }

    public void clearWindows() {
        int size = spawnPoints.size();
        for(int i = size - 1; i >= 0; i--) {
            ISpawnpointContainer container = spawnPoints.get(i);
            if(container instanceof Window) {
                spawnPoints.remove(i);
            }
        }
    }

    public void clearRoomSpawns() {
        int size = spawnPoints.size();
        for(int i = size - 1; i >= 0; i--) {
            ISpawnpointContainer container = spawnPoints.get(i);
            if(container instanceof SpawnPoint) {
                spawnPoints.remove(i);
            }
        }
    }
}
