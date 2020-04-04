package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.map.Door;
import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.map.Window;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.github.zap.zombiesplugin.shop.Shop;

import java.util.ArrayList;

public class RoomData implements IMapData<Room> {
    public String name;
    public ArrayList<DoorData> doors;
    public ArrayList<WindowData> windows;
    public ArrayList<SpawnPointData> spawnPoints;
    public ArrayList<ShopData> shops;

    public RoomData() {}

    public RoomData(Room from) {
        name = from.getName();
        doors = new ArrayList<>();
        windows = new ArrayList<>();
        spawnPoints = new ArrayList<>();
        shops = new ArrayList<>();

        for(Door door : from.getDoors()) {
            doors.add(new DoorData(door));
        }

        for(Window window : from.getWindows()) {
            windows.add(new WindowData(window));
        }

        for(SpawnPoint spawnPoint : from.getSpawnPoints()) {
            spawnPoints.add(new SpawnPointData(spawnPoint));
        }

        for(Shop shop : from.getShops()) {
            shops.add(new ShopData(shop));
        }
    }

    @Override
    public Room load() {
        Room result = new Room(name);

        for(DoorData data : doors) {
            result.add(data.load());
        }

        for(WindowData data : windows) {
            result.add(data.load());
        }

        for(SpawnPointData data : spawnPoints) {
            result.add(data.load());
        }

        for(ShopData data : shops) {
            result.add(data.load());
        }

        return result;
    }
}
