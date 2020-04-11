package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.github.zap.zombiesplugin.shop.Shop;
import org.bukkit.Location;

import java.util.ArrayList;

public class Room implements ISpawnpointContainer {
    private final String name;
    private GameMap map;
    private ArrayList<Door> doors;
    private ArrayList<Window> windows;
    private ArrayList<SpawnPoint> spawnPoints;
    private ArrayList<Shop> shops;
    private MultiBoundingBox bounds;
    private boolean open;

    public Room(String name, MultiBoundingBox bounds, GameMap map) {
        this.name = name;
        this.map = map;
        doors = new ArrayList<>();
        windows = new ArrayList<>();
        spawnPoints = new ArrayList<>();
        shops = new ArrayList<>();
        this.bounds = bounds;
    }

    public void add(Door door) {
        doors.add(door);
    }

    public void add(Window window) {
        windows.add(window);
    }

    public void add(SpawnPoint spawnPoint) {
        spawnPoints.add(spawnPoint);
    }

    public void add(Shop shop) {
        shops.add(shop);
    }

    public boolean isInBounds(Location location) {
        return bounds.isInBound(location);
    }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Door> getDoors() {
        return doors;
    }

    public ArrayList<Shop> getShops() {
        return shops;
    }

    public MultiBoundingBox getBounds() {
        return bounds;
    }

    public ArrayList<Window> getWindows() {
        return windows;
    }

    public GameMap getMap() {
        return map;
    }

    @Override
    public boolean canSpawn() {
        return open;
    }

    @Override
    public int size() {
        return spawnPoints.size();
    }

    @Override
    public SpawnPoint getSpawnpoint(int index) {
        return spawnPoints.get(0);
    }

    @Override
    public ArrayList<SpawnPoint> getSpawnpoints() {
        return spawnPoints;
    }
}
