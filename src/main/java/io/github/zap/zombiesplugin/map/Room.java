package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;

import java.util.ArrayList;
import java.util.HashMap;

public class Room {
    private final String name;
    private ArrayList<SpawnPoint> spawnPoints;
    private boolean open;

    public Room(String name) {
        this.name = name;
        spawnPoints = new ArrayList<>();
    }

    public void add(Window window) {
        spawnPoints.add(window.getSpawnPoint());
    }

    public void add(SpawnPoint spawnPoint) {
        spawnPoints.add(spawnPoint);
    }

    public String getName() { return name; }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
    }

    public ArrayList<SpawnPoint> getSpawnPoints() { return spawnPoints; }
}
