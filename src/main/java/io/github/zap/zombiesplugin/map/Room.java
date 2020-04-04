package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.github.zap.zombiesplugin.shop.Shop;
import org.bukkit.Location;

import java.util.ArrayList;

public class Room {
    private final String name;
    private ArrayList<Door> doors;
    private ArrayList<Window> windows;
    private ArrayList<SpawnPoint> spawnPoints;
    private ArrayList<Shop> shops;
    private boolean open;

    public Room(String name) {
        this.name = name;
        doors = new ArrayList<>();
        windows = new ArrayList<>();
        spawnPoints = new ArrayList<>();
        shops = new ArrayList<>();
    }

    public Window getWindowAt(Location location) {
        for(Window window : windows) {
            if(window.getWindowBounds().isInBound(location)) return window;
        }
        return null;
    }

    public void add(Door door) { doors.add(door); }

    public void add(Window window) { windows.add(window); }

    public void add(SpawnPoint spawnPoint) { spawnPoints.add(spawnPoint); }

    public void add(Shop shop) {
        shops.add(shop);
    }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
    }

    public String getName() { return name; }

    public ArrayList<Door> getDoors() { return doors; }

    public ArrayList<Window> getWindows() { return windows; }

    public ArrayList<SpawnPoint> getSpawnPoints() { return spawnPoints; }

    public ArrayList<Shop> getShops() { return shops; }
}
