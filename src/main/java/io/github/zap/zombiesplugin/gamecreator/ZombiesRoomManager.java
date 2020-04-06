package io.github.zap.zombiesplugin.gamecreator;

import fr.minuskube.inv.SmartInventory;
import io.github.zap.zombiesplugin.gamecreator.gui.GuiRoomList;
import io.github.zap.zombiesplugin.manager.GameDifficulty;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ZombiesRoomManager {
    public List<GameRoom> rooms = new ArrayList<>();
    public List<ZombiesRoomUser> users = new ArrayList<>();


    public void openGUI (Player p) {
        ZombiesRoomUser user = getContainingUser(p);

        //TODO: Test
        GameRoom gr = new GameRoom();
        gr.setRoomName("Bro test room");
        gr.setRoomCapacity(4);
        gr.setPlayers(Arrays.asList(user));
        gr.setHost(user);
        gr.setPublic(true);
        gr.setRoomID(123112421);
        gr.setMapName("Dead end");
        gr.setDiff(GameDifficulty.NORMAL);
        rooms.add(gr);


        if (user.currentRoom == null) {
            openListGui(user);
        }
    }

    public void createGame() {

    }

    private void openListGui(ZombiesRoomUser user) {
        String title = "Zombies lobby GUI";
        if (user.search != null) {
            title += " - Search for: " + user.search;
        }

        SmartInventory.builder()
                .title(ChatColor.GREEN + title)
                .size(6,9)
                .provider(new GuiRoomList(user))
                .build()
                .open(user.player);
    }


    private void openRoomGui() {

    }

    @NotNull
    private ZombiesRoomUser getContainingUser(Player p) {
        for (ZombiesRoomUser u : users) {
            if(u.player.getUniqueId() == p.getUniqueId()) {
                // In case the player log out and log back in.
                u.player = p;
                return u;
            }
        }
        ZombiesRoomUser user = new ZombiesRoomUser(p);
        users.add(user);
        return user;
    }
}
