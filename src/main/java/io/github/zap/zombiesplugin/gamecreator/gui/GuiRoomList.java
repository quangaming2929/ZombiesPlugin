package io.github.zap.zombiesplugin.gamecreator.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.gamecreator.GameRoom;
import io.github.zap.zombiesplugin.gamecreator.ZombiesRoomUser;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.zap.zombiesplugin.gamecreator.gui.CommonVisual.*;

/**
 * TODO: Re-draw only when needed by mimicking IPropertyChanged in C# System.ComponentModel
 */
public class GuiRoomList implements InventoryProvider {
    private static final int ROOM_UPDATE_INTERVAL = 60;

    public final ZombiesRoomUser user;

    public GuiRoomList(ZombiesRoomUser user) {
        this.user = user;
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        inventoryContents.set(0, 0, ClickableItem.of(filterVisual(), e -> {
            player.sendMessage(ChatColor.GREEN + "You clicked filter");
        }));

        inventoryContents.set(0, 8, ClickableItem.of(createRoomVisual(), e -> {
            ZombiesPlugin.instance.getRoomManager().createRoomClick(user);
        }));

        inventoryContents.set(5, 4, ClickableItem.of(searchVisual(), e -> {
            player.sendMessage(ChatColor.GREEN + "You clicked search");
        }));
        updatePage(player, inventoryContents);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
        inventoryContents.set(5, 0, ClickableItem.of(previousPageVisual(user.currentPage), e -> {
            user.currentPage -= 1;
            updatePage(player, inventoryContents);
        }));

        inventoryContents.set(5, 8, ClickableItem.of(nextPageVisual(user.currentPage), e -> {
            user.currentPage += 1;
            updatePage(player, inventoryContents);
        }));

        // Measure the tick for this inventory
        int tick = inventoryContents.property("tick", 0);
        inventoryContents.setProperty("tick", tick += 1);

        if (tick % ROOM_UPDATE_INTERVAL == 0) {
            updatePage(player, inventoryContents);
        }
    }

    private void updatePage(Player player, InventoryContents contents) {
        List<GameRoom> rooms = ZombiesPlugin.instance.getRoomManager().rooms;
        contents.fillRect(1, 0, 4, 8, ClickableItem.empty(null));

        // list view item 1
        int progCurrentPage = user.currentPage - 1; // User see this as 1 based index but we use 0 based index
        if (rooms.size() > progCurrentPage * 2) {
            contents.set(2,4, ClickableItem.empty(null));

            GameRoom room1 = rooms.get(progCurrentPage * 2);
            createRoomVisual(room1, 0, player, contents);

            // list view item 2
            if (rooms.size() > progCurrentPage * 2 + 1) {
                GameRoom room2 = rooms.get(progCurrentPage * 2 + 1);
                createRoomVisual(room2, 1, player, contents);
            }
        } else { // Show nothing to show here
            fillRegion(1, 0, 4, 8, ClickableItem.empty(null), contents);
            contents.set(2,4, ClickableItem.empty(noRoomVisual()));
        }
    }

    private void createRoomVisual (GameRoom room, int index, Player player, InventoryContents inventoryContents) {
        inventoryContents.set(1 + index * 2, 0, ClickableItem.empty(createRoomInfoVisual(room)));

        ItemMeta listMeta = new ItemStack(Material.BLACK_GLAZED_TERRACOTTA, 1).getItemMeta();
        listMeta.setDisplayName(ChatColor.GOLD + room.getRoomName() + "'s players: ");
        List<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.WHITE + "Host: " + ChatColor.GOLD + room.getHost().player.getDisplayName());
        loreList.add(""); // Blank line
        loreList.add(ChatColor.WHITE + "Players list (" + room.getPlayerCount() +  "/" + room.getRoomCapacity() + "):");

        for (int playerIndex = 0; playerIndex < Math.min(room.getPlayerCount(), 10); playerIndex++) {
            ZombiesRoomUser cUser = room.getPlayers().get(playerIndex);
            if (cUser == room.getHost()) {
                loreList.add(ChatColor.GRAY + " ◼ " + ChatColor.GOLD + cUser.player.getName() + ChatColor.GREEN + " (host)");
            } else {
                loreList.add(ChatColor.GRAY + " ◼ " + cUser.player.getName());
            }
        }

        if (room.getPlayerCount() > 10) {
            loreList.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "And " + (room.getPlayerCount() - 10) + " more...");
            loreList.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Use /zombiesrooms pl " + room.getRoomID() + " to get all players");
        }

        listMeta.setLore(loreList);

        int verticalIndex = 2 + index * 2;
        if (room.getRoomCapacity() < 9) {

            for (int i = 0; i < room.getPlayerCount(); i++) {
                Material mat = room.getPlayers().get(i) == room.getHost() ? Material.ORANGE_TERRACOTTA : Material.GREEN_TERRACOTTA;
                ItemStack playerIndicator = new ItemStack(mat, 1);
                playerIndicator.setItemMeta(listMeta);

                inventoryContents.set(verticalIndex, i, ClickableItem.empty(playerIndicator));
            }

            for (int i = room.getPlayerCount(); i < room.getRoomCapacity(); i++) {
                ItemStack emptyPlayer = new ItemStack(Material.WHITE_TERRACOTTA, 1);
                emptyPlayer.setItemMeta(listMeta);

                inventoryContents.set(verticalIndex, i, ClickableItem.empty(emptyPlayer));
            }
        } else { // Mega big room
            for (int i = 0; i < 8; i++) {
                ItemStack playerIndicator = new ItemStack(Material.PINK_TERRACOTTA, 1);
                playerIndicator.setItemMeta(listMeta);

                inventoryContents.set(verticalIndex, i, ClickableItem.empty(playerIndicator));
            }
        }

        inventoryContents.set(verticalIndex, 7, ClickableItem.of(specVisual(room.getRoomName()), e -> {
            // TODO: Test code
            ZombiesPlugin.instance.getRoomManager().attemptSpecClick(user, room);
        }));

        inventoryContents.set(verticalIndex, 8, ClickableItem.of(joinVisual(room.getRoomName()), e -> {
            // TODO: Test code
            ZombiesPlugin.instance.getRoomManager().attemptJoinClick(user, room);
        }));
    }

    private ItemStack createRoomInfoVisual (GameRoom room) {
        return createVisualItem(
                Material.ZOMBIE_HEAD,
                1,
                ChatColor.WHITE + "Room name: " + ChatColor.GREEN + room.getRoomName(),
                Arrays.asList(getRoomDetailLore(room)));
    }

    private ItemStack specVisual (String roomName) {
        String[] lore = new String[] {
                ChatColor.GRAY + "Click here to spectate ",
                ChatColor.GOLD + roomName + ChatColor.GRAY + ". If the room is",
                ChatColor.GRAY + "private, we will send a request to the",
                ChatColor.GRAY + "room host"
        };

        return createVisualItem(
                Material.ENDER_EYE,
                1,
                ChatColor.GOLD + "Spectate",
                Arrays.asList(lore));
    }

    private ItemStack joinVisual (String roomName) {
        ItemStack s = PlayerHeadFactory.getArrowRight();
        ItemMeta meta = s.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Join");
        String[] lore = new String[] {
                ChatColor.GRAY + "Click here to join ",
                ChatColor.GOLD + roomName + ChatColor.GRAY + ". If the game is",
                ChatColor.GRAY + "private, we will send a request to the",
                ChatColor.GRAY + "room host"
        };

        meta.setLore(Arrays.asList(lore));
        s.setItemMeta(meta);

        return s;
    }

    private ItemStack noRoomVisual () {
        String[] lore = new String[] {
                ChatColor.GRAY + "Nothing to show here, you can",
                ChatColor.GRAY + "try to create your own room"
        };

        return createVisualItem(
                Material.BARRIER,
                1,
                ChatColor.RED + "No room",
                Arrays.asList(lore));
    }



    private ItemStack filterVisual () {
        String[] lore = new String[] {
                ChatColor.GRAY + "Current filters: ",
                ChatColor.WHITE + "Map: " + ChatColor.GREEN + (user.mapNameFilter == null ? "All maps" : user.mapNameFilter),
                ChatColor.WHITE + "Mode: " + ChatColor.GREEN + (user.modeFilter == null ? "All maps" : user.modeFilter),
                ChatColor.WHITE + "Show full room: " + ChatColor.GREEN + user.showFull,
                ChatColor.WHITE + "Show private room: " + ChatColor.GREEN + user.showPrivate,
                ChatColor.WHITE + "Show started room: " + ChatColor.GREEN + user.showStarted,
                "", // Blank line
                ChatColor.GRAY + "Click to change filters"
        };

        return createVisualItem(
                Material.HOPPER,
                1,
                ChatColor.GREEN + "Filters...",
                Arrays.asList(lore));
    }

    private ItemStack createRoomVisual () {
        return createVisualItem(
                Material.MAP,
                1,
                ChatColor.GREEN + "Create room",
                Arrays.asList(ChatColor.GRAY + "Click to create a new room!"));
    }

    private ItemStack searchVisual() {
        List<String> lore = new ArrayList<>();
        if(user.search != null) {
            lore.add(ChatColor.WHITE + "Search for: " + ChatColor.AQUA + user.search);
            lore.add(""); // Blank line
        }
        lore.add(ChatColor.GRAY + "Click here to enter text");

        return createVisualItem(
                Material.WRITABLE_BOOK,
                1,
                ChatColor.GREEN + "Search", lore);
    }
}
