package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.commands.mapeditor.ContextManager;
import io.github.zap.zombiesplugin.commands.mapeditor.IEditorContext;
import io.github.zap.zombiesplugin.map.*;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.github.zap.zombiesplugin.shop.Shop;
import io.github.zap.zombiesplugin.utils.Tuple;

import java.util.ArrayList;

public class RoomData implements IMapData<Room>, IEditorContext {
    public String name;
    public ArrayList<DoorData> doors;
    public ArrayList<WindowData> windows;
    public ArrayList<SpawnPointData> spawnPoints;
    public ArrayList<ShopData> shops;
    public MultiBoundingBoxData bounds;

    private GameMapData parent;

    public RoomData() {}

    public RoomData(Room from, GameMapData parent) {
        this.parent = parent;
        name = from.getName();
        doors = new ArrayList<>();
        windows = new ArrayList<>();
        spawnPoints = new ArrayList<>();
        shops = new ArrayList<>();

        for(Door door : from.getDoors()) {
            doors.add(new DoorData(door));
        }

        for(Window window : from.getWindows()) {
            windows.add(new WindowData(window, this));
        }

        for(SpawnPoint spawnPoint : from.getSpawnpoints()) {
            spawnPoints.add(new SpawnPointData(spawnPoint, this));
        }

        for(Shop shop : from.getShops()) {
            shops.add(new ShopData(shop));
        }

        bounds = new MultiBoundingBoxData(from.getBounds());
    }

    @Override
    public Room load(Object args) {
        if(args instanceof GameMap) {
            GameMap map = (GameMap)args;
            Room result = new Room(name, bounds.load(null), map);

            for(DoorData data : doors) {
                result.add(data.load(result));
            }

            for(WindowData data : windows) {
                result.add(data.load(result));
            }

            for(SpawnPointData data : spawnPoints) {
                result.add(data.load(result));
            }

            for(ShopData data : shops) {
                result.add(data.load(result));
            }

            return result;
        }
        else {
            throw new IllegalArgumentException("args must be an instance of GameMap");
        }
    }

    @Override
    public Tuple<Boolean, String> canExecute(ContextManager session, String commandName, String[] args) {
        if(name.equals("room")) {
            if(args.length == 2) {

            }
        }
        return null;
    }

    @Override
    public void execute(ContextManager session, String name, String[] args) {

    }
}
