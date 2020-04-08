package io.github.zap.zombiesplugin.gamecreator;

import io.github.zap.zombiesplugin.manager.GameDifficulty;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.manager.GameState;
import io.github.zap.zombiesplugin.map.GameMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Zombie;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameRoom {
    private Random rand = new Random();

    public GameRoom(int roomID) {
        this.roomID = roomID;
    }

    private final int roomID;
    private String roomName;
    private String roomPassword = "";
    private int roomCapacity = 4;
    private GameManager gameManager;

    private ZombiesRoomUser host;
    // Include host here
    private List<ZombiesRoomUser> players = new ArrayList<>();
    private List<ZombiesRoomUser> spectators = new ArrayList<>();

    // States
    private int currentMapPage = 1;
    private int currentModePage = 1;
    private GameMap selectedMap;
    private GameDifficulty selectedDiff;
    private List<ZombiesRoomUser> readyUser = new ArrayList<>();
    private List<ZombiesRoomUser> notReadyUser = new ArrayList<>();

    public void ready(ZombiesRoomUser user) {
        user.isReady = true;
        readyUser.add(user);
        notReadyUser.remove(user);
    }

    public void notReady(ZombiesRoomUser user) {
        user.isReady = false;
        notReadyUser.add(user);
        readyUser.remove(user);
    }

    public int getReadyCount () {
        return readyUser.size();
    }

    public boolean isAllReady () {
        return notReadyUser.size() == 0;
    }

    public boolean isHostReady() {
        return host.isReady;
    }

    public void broadCastRoom (String message) {
        for (ZombiesRoomUser u : getPlayers()) {
            u.player.sendMessage(message);
        }
    }

    public static String getRoomPrefix() {
        return ChatColor.DARK_BLUE + "Room > " + ChatColor.WHITE;
    }

    public int getRoomID() {
        return roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getMapName() {
        return getSelectedMap() == null ? "Not specified" : getSelectedMap().getName();
    }

    public boolean isPublic() {
        return roomPassword.equals("");
    }

    public ZombiesRoomUser getHost() {
        return host;
    }

    public void setHost(ZombiesRoomUser host) {
        this.host = host;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public void addPlayer (ZombiesRoomUser user) {
        players.add(user);
        notReadyUser.add(user);

        user.isReady = false;
        user.currentRoom = this;
    }

    public void removePlayer (ZombiesRoomUser user) {
        players.remove(user);
        if(user.isReady) {
            readyUser.remove(user);
        } else {
            notReadyUser.remove(user);
        }

        user.leaveRoom();

        // Change host if the host leaves this room
        if (user == getHost() && getPlayerCount() > 0) {
            int newHostIndex = rand.nextInt(getPlayerCount());
            ZombiesRoomUser newHost = getPlayers().get(newHostIndex);
            setHost(newHost);


            String msg = ChatColor.AQUA + "The previous host: " + ChatColor.RED + user.player.getDisplayName() + ChatColor.AQUA +
                    " left the room so we assign " + ChatColor.GREEN + newHost.player.getDisplayName() + ChatColor.AQUA +
                    " as the new host for this room!";
            broadCastRoom(msg);
        }
    }

    public List<ZombiesRoomUser> getPlayers() {
        return players;
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
        //TODO: Kick exceeding players
        this.roomCapacity = roomCapacity;

        // If host position lies on the exceed section we need to store them else where
        ZombiesRoomUser host = null;
        while (this.getPlayerCount() > roomCapacity) {
            ZombiesRoomUser user = this.getPlayers().get(this.getPlayerCount() - 1);
            if (user == getHost()) {
                host = user;
            } else {
                removePlayer(user);
                user.player.sendMessage(ChatColor.RED + "You have been kicked because the current room is full!");
            }
        }

        if (host != null) {
            removePlayer(this.getPlayers().get(this.getPlayerCount() - 1));
            this.getPlayers().add(host);
        }
    }

    public int getCurrentMapPage() {
        return currentMapPage;
    }

    public void setCurrentMapPage(int currentMapPage) {
        this.currentMapPage = currentMapPage;
    }

    public int getCurrentModePage() {
        return currentModePage;
    }

    public void setCurrentModePage(int currentModePage) {
        this.currentModePage = currentModePage;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public GameState getGameState() {
        return gameManager == null ? GameState.PREGAME : gameManager.getState();
    }

    public boolean setGameState(GameState state) {
        if (gameManager != null) {
            gameManager.setState(state);
            return true;
        }

        return false;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }

    public List<ZombiesRoomUser> getReadyUsers() {
        return readyUser;
    }

    public List<ZombiesRoomUser> getNotReadyUsers() {
        return notReadyUser;
    }

    public GameMap getSelectedMap() {
        return selectedMap;
    }

    public void setSelectedMap(GameMap selectedMap) {
        this.selectedMap = selectedMap;
    }

    public GameDifficulty getSelectedDiff() {
        return selectedDiff;
    }

    public void setSelectedDiff(GameDifficulty selectedDiff) {
        this.selectedDiff = selectedDiff;
    }
}
