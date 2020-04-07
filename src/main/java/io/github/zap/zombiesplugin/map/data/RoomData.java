package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.map.*;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.github.zap.zombiesplugin.shop.Shop;

import java.util.ArrayList;

public class RoomData implements IMapData<Room> {
    public String name;
    public ArrayList<DoorData> doors;
    public ArrayList<WindowData> windows;
    public ArrayList<SpawnPointData> spawnPoints;
    public ArrayList<ShopData> shops;
    public MultiBoundingBoxData bounds;

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

        for(ISpawnpointContainer container : from.getSpawnPoints()) {
            if(container instanceof Window) {
                windows.add(new WindowData((Window)container));
            }
            else if(container instanceof SpawnPoint) {
                spawnPoints.add(new SpawnPointData((SpawnPoint)container));
            }
        }

        for(Shop shop : from.getShops()) {
            shops.add(new ShopData(shop));
        }
    }

    @Override
    public Room load(Object args) {
        GameMap parent;
        if(args instanceof GameMap) {
            parent = (GameMap)args;
        }
        else {
            throw new IllegalArgumentException("args must be an instance of GameMap");
        }

        Room result = new Room(name, bounds.load(null), parent.getLookupHelper());
        for(DoorData data : doors) {
            result.add(data.load(null));
        }

        for(WindowData data : windows) {
            result.add(data.load(null));
        }

        for(SpawnPointData data : spawnPoints) {
            result.add(data.load(null));
        }

        for(ShopData data : shops) {
            result.add(data.load(null));
        }

        return result;
    }
}
