package io.github.zap.zombiesplugin.commands;

import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.data.GameMapData;
import io.github.zap.zombiesplugin.map.data.RoomData;
import io.github.zap.zombiesplugin.map.data.WindowData;
import org.bukkit.Location;

import java.util.UUID;

public class EditorSession {
    private UUID uuid;
    private GameMapData mapData;

    private Location previousSelection;
    private Location lastSelection;

    private WindowData currentWindow;
    private RoomData currentRoom;

    private boolean isSaved;

    public EditorSession(UUID uuid, String mapName) {
        this.uuid = uuid;
        mapData = new GameMapData(new GameMap(mapName));
    }

    public EditorSession(UUID uuid, GameMap map) {
        this.uuid = uuid;
        mapData = new GameMapData(map);
    }

    public UUID getUuid() {
        return uuid;
    }

    public GameMapData getMapData() {
        return mapData;
    }

    public WindowData getCurrentWindow() {
        return currentWindow;
    }

    public RoomData getCurrentRoom() {
        return currentRoom;
    }

    public Location getPreviousSelection() {
        return previousSelection;
    }

    public Location getLastSelection() {
        return lastSelection;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setCurrentWindow(WindowData currentWindow) {
        previousSelection = null;
        lastSelection = null;
        isSaved = false;
        this.currentWindow = currentWindow;
    }

    public void setCurrentRoom(RoomData currentRoom) {
        previousSelection = null;
        lastSelection = null;
        isSaved = false;
        this.currentWindow = null;
        this.currentRoom = currentRoom;
    }
    public void setPreviousSelection(Location previousSelection) {
        this.previousSelection = previousSelection;
        isSaved = false;
    }

    public void setLastSelection(Location lastSelection) {
        this.lastSelection = lastSelection;
        isSaved = false;
    }

    public void save() {

    }
}