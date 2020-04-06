package io.github.zap.zombiesplugin.gamecreator;

import io.github.zap.zombiesplugin.manager.GameDifficulty;

import java.util.List;

public class GameRoom {
    private int roomID;
    private String roomName;
    private String mapName; // could use mapID here
    private GameDifficulty diff;
    private boolean isPublic;
    private int roomCapacity;

    private ZombiesRoomUser host;
    // Include host here
    private List<ZombiesRoomUser> players;
    private List<ZombiesRoomUser> spectators;

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public GameDifficulty getDiff() {
        return diff;
    }

    public void setDiff(GameDifficulty diff) {
        this.diff = diff;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public ZombiesRoomUser getHost() {
        return host;
    }

    public void setHost(ZombiesRoomUser host) {
        this.host = host;
    }

    public List<ZombiesRoomUser> getPlayers() {
        return players;
    }

    public void setPlayers(List<ZombiesRoomUser> players) {
        this.players = players;
    }

    public List<ZombiesRoomUser> getSpectators() {
        return spectators;
    }

    public void setSpectators(List<ZombiesRoomUser> spectators) {
        this.spectators = spectators;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }
}
