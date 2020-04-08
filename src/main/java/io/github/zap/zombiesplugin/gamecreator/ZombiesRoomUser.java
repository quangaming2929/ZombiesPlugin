package io.github.zap.zombiesplugin.gamecreator;

import fr.minuskube.inv.SmartInventory;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ZombiesRoomUser {
    public Player player;

    // Null = all maps
    public String mapNameFilter = null;
    public String modeFilter = null;
    public boolean showFull = false;
    public boolean showPrivate = false;
    public boolean showStarted = false;
    public int currentPage = 1;

    public String search;
    // Can be null TODO: Remember to change this else false report will occur
    public GameRoom currentRoom;
    // Can be null
    public GameRoom currentSpecRoom;
    // Ready state in a room, false if not in any room
    public boolean isReady;

    /**
     * Set the current room and spec room to null
     */
    public void leaveRoom () {
        currentSpecRoom = null;
        currentRoom = null;
    }

    public ZombiesRoomUser(Player p) {
        this.player = p;
    }
}
