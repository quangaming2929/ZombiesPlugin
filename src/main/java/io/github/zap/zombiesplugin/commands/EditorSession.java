package io.github.zap.zombiesplugin.commands;

import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.map.Window;
import io.github.zap.zombiesplugin.map.data.RoomData;
import io.github.zap.zombiesplugin.map.data.WindowData;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

public class EditorSession {
    private UUID id;
    private String mapName;

    private RoomData currentRoom;
    private WindowData currentWindow;

    private HashMap<String,Location> boundsMappings;

    public EditorSession(UUID id, String mapName) {
        this.id = id;
        this.mapName = mapName;
        boundsMappings = new HashMap<>();
    }

    public UUID getId() {
        return id;
    }

    public String getMapName() {
        return mapName;
    }

    public Location getLocation(String name) {
        return boundsMappings.getOrDefault(name, null);
    }

    public void putLocation(String name, Location location) {
        if(boundsMappings.containsKey(name)) {
            boundsMappings.remove(name);
            boundsMappings.put(name, location);
        }
        else boundsMappings.put(name, location);
    }

    public RoomData getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(RoomData currentRoom) {
        this.currentRoom = currentRoom;
        currentWindow = null;
    }

    public WindowData getCurrentWindow() {
        return currentWindow;
    }

    public void setCurrentWindow(WindowData currentWindow) {
        this.currentWindow = currentWindow;
    }
}
