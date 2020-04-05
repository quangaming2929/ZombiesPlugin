package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.manager.GameState;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;

import java.util.HashMap;

public class LookupHelper {
    private HashMap<SpawnPoint, Window> spawnPointToWindow;
    private HashMap<SpawnPoint, Room> spawnPointToRoom;
    private HashMap<Window, Room> windowToRoom;
    private HashMap<Room, GameMap> roomToGameMap;

    public LookupHelper() {
        spawnPointToWindow = new HashMap<>();
        spawnPointToRoom = new HashMap<>();
        windowToRoom = new HashMap<>();
        roomToGameMap = new HashMap<>();
    }

    public Window getWindow(SpawnPoint point) {
        System.out.println("getWindow called");
        if(spawnPointToWindow.containsKey(point)) {
            System.out.println("returning window");
            return spawnPointToWindow.get(point);
        }
        System.out.println("returning null");
        return null;
    }

    public Room getRoom(SpawnPoint point) {
        if(spawnPointToRoom.containsKey(point)) {
            return spawnPointToRoom.get(point);
        }
        else if(spawnPointToWindow.containsKey(point)) {
            Window window = spawnPointToWindow.get(point);
            if(windowToRoom.containsKey(window)) {
                return windowToRoom.get(window);
            }
        }
        return null;
    }

    public GameMap getMap(SpawnPoint point) {
        if(spawnPointToWindow.containsKey(point)) {
            Window window = spawnPointToWindow.get(point);
            if(windowToRoom.containsKey(window)) {
                Room room = windowToRoom.get(window);
                if(roomToGameMap.containsKey(room)) {
                    return roomToGameMap.get(room);
                }
            }
        }
        return null;
    }

    public void addMapping(SpawnPoint point, Window window) {
        spawnPointToWindow.put(point, window);
    }

    public void addMapping(SpawnPoint point, Room room) {
        spawnPointToRoom.put(point, room);
    }

    public void addMapping(Window window, Room room) {
        windowToRoom.put(window, room);
    }

    public void addMapping(Room room, GameMap map) {
        roomToGameMap.put(room, map);
    }
}
