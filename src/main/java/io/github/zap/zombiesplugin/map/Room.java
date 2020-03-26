package io.github.zap.zombiesplugin.map;

public class Room {
    private GameMap map;
    private boolean open;

    public Room(GameMap map) {
        this.map = map;
    }

    public GameMap getMap() {
        return map;
    }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
    }
}
