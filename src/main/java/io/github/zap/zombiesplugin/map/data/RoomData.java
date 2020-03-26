package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.map.Room;

public class RoomData {
    public String mapName;

    public RoomData() {}

    public RoomData(Room from) {
        mapName = from.getMap().name;
    }
}
