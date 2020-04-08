package io.github.zap.zombiesplugin.gamecreator;

import fr.minuskube.inv.SmartInventory;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.gamecreator.gui.CommonVisual;
import io.github.zap.zombiesplugin.gamecreator.gui.GuiRoom;
import io.github.zap.zombiesplugin.gamecreator.gui.GuiRoomList;
import io.github.zap.zombiesplugin.manager.GameDifficulty;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class ZombiesRoomManager {
    public List<GameRoom> rooms = new ArrayList<>();
    public List<ZombiesRoomUser> users = new ArrayList<>();

    private AtomicInteger idProvider = new AtomicInteger(100000);

    public void openGUI (Player p) {
        p.closeInventory();
        ZombiesRoomUser user = getContainingUser(p);

        if (user.currentRoom == null && user.currentSpecRoom == null) {
            openListGui(user);
        } else {
            openRoomGui(user);
        }
    }

    public GameRoom createNewRoom(ZombiesRoomUser user) {
        GameRoom room = new GameRoom(idProvider.getAndIncrement());
        room.setRoomName(user.player.getDisplayName() + "'s room");
        room.setHost(user);
        room.addPlayer(user);

        // add to rooms sorta database
        rooms.add(room);
        return room;
    }

    private void openListGui(ZombiesRoomUser user) {
        String title = "Zombies lobby GUI";
        if (user.search != null) {
            title += " - Search for: " + user.search;
        }

        SmartInventory.builder()
                .title(title)
                .size(6,9)
                .provider(new GuiRoomList(user))
                .build()
                .open(user.player);
    }


    private void openRoomGui(ZombiesRoomUser user) {
        String title = "Room: ";
        GameRoom room = null;
        if (user.currentRoom != null) {
            title += user.currentRoom.getRoomID();
            room = user.currentRoom;
        } else {
            title += user.currentSpecRoom.getRoomID() + " (Spectating)";
            room = user.currentSpecRoom;
        }

        SmartInventory.builder()
                .title(title)
                .size(6, 9)
                .provider(new GuiRoom(room ,user))
                .build()
                .open(user.player);
    }

    private void joinGame (ZombiesRoomUser user, GameRoom room) {
        user.leaveRoom();
        room.addPlayer(user);
        user.currentRoom = room;
        openRoomGui(user);
    }

    private void specGame (ZombiesRoomUser user, GameRoom room) {
        user.leaveRoom();
        room.getSpectators().add(user);
        user.currentSpecRoom = room;
        openRoomGui(user);
    }

    /**
     * Use to get user input
     */
    private void createPasswordBox (Player p, BiFunction<Player, String, AnvilGUI.Response> onComplete ) {
        ItemStack i = CommonVisual.createVisualItem(
                Material.END_ROD,
                1,
                "",
                Arrays.asList(ChatColor.GRAY + "Click the right slot to submit"));

        new AnvilGUI.Builder()
                .onComplete(onComplete)
                .title("Password protected!")
                .text("Please enter password here!")
                .plugin(ZombiesPlugin.instance)
                .item(i)
                .open(p);
    }

    // Some events will be routed to this class, usually those events that requires to navigate to another gui
    public void createRoomClick(ZombiesRoomUser user) {
        GameRoom room = createNewRoom(user);
        openRoomGui(user);
    }

    public void leaveRoomClick(ZombiesRoomUser user) {
        GameRoom currentRoom = user.currentRoom != null ? user.currentRoom : user.currentSpecRoom;

        // Remove user from player/spec list
        if (user.currentRoom != null) {
            user.currentRoom.removePlayer(user);
        } else {
            user.currentSpecRoom.getSpectators().remove(user);
        }

        // Delete the room if the current room is empty
        if (currentRoom.getPlayerCount() == 0) {
            rooms.remove(currentRoom);
        }

        openListGui(user);
    }

    public void attemptJoinClick(ZombiesRoomUser user, GameRoom game) {
        if (rooms.contains(game)) {
            if (game.isPublic()) {
                joinGame(user, game);
            } else { // Password protected room
                createPasswordBox(user.player, (p,s) -> {
                    if (game.isPublic() || game.getRoomPassword().equals(s)) {
                        joinGame(user, game);
                        return AnvilGUI.Response.close();
                    } else {
                        return AnvilGUI.Response.text("Wrong password, please try again");
                    }
                });
            }
        } else {
            user.player.sendMessage(ChatColor.RED + "The room is no longer exist!");
        }
    }

    public void attemptSpecClick(ZombiesRoomUser user, GameRoom room) {
        user.player.sendMessage(ChatColor.RED + "unimplemented feature :(");

        // TODO: Implement the spec feature, TODO2: Remember to check for data and gui desync
        /*if (room.isPublic()) {
            specGame(user, room);
        } else { // Password protected room
            createPasswordBox(user.player, (p,s) -> {
                if (room.isPublic() || room.getRoomPassword().equals(s)) {
                    specGame(user, room);
                    return AnvilGUI.Response.close();
                } else {
                    return AnvilGUI.Response.text("Wrong password, please try again");
                }
            });
        }*/
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
