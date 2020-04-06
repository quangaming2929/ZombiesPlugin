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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            player.sendMessage(ChatColor.GREEN + "You clicked create room");
        }));

        inventoryContents.set(5, 4, ClickableItem.of(searchVisual(), e -> {
            player.sendMessage(ChatColor.GREEN + "You clicked search");
        }));

        inventoryContents.set(5, 0, ClickableItem.of(previousPageVisual(user.currentPage), e -> {
            player.sendMessage(ChatColor.GREEN + "You clicked prev");
        }));

        inventoryContents.set(5, 8, ClickableItem.of(nextPageVisual(user.currentPage), e -> {
            player.sendMessage(ChatColor.GREEN + "You clicked next");
        }));

        updatePage(player, inventoryContents);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
        // Measure the tick for this inventory
        int tick = inventoryContents.property("tick", 0);
        inventoryContents.setProperty("tick", tick += 1);

        if (tick % ROOM_UPDATE_INTERVAL == 0) {
            updatePage(player, inventoryContents);
        }
    }

    private void updatePage(Player player, InventoryContents contents) {

        List<GameRoom> rooms = ZombiesPlugin.instance.getRoomManager().rooms;

        // list view item 1
        int progCurrentPage = user.currentPage - 1; // User see this as 1 based index but we use 0 based index
        if (rooms.size() > progCurrentPage * 2 + 1) {
            GameRoom room1 = rooms.get(progCurrentPage * 2);
            createRoomVisual(room1, 0, player, contents);

            // list view item 2
            if (rooms.size() > progCurrentPage * 2 + 2) {
                GameRoom room2 = rooms.get(progCurrentPage * 2 + 1);
                createRoomVisual(room2, 0, player, contents);
            }
        } else { // Show nothing to show, here
            contents.set(2,4, ClickableItem.empty(noRoomVisual()));
        }
    }

    private void createRoomVisual (GameRoom room, int index, Player player, InventoryContents inventoryContents) {
        ItemStack zombie = new ItemStack(Material.ZOMBIE_HEAD, 1);
        ItemMeta meta = zombie.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Room name: " + ChatColor.GREEN + room.getRoomName());
        String[] loreRoom = new String[] {
                ChatColor.DARK_GRAY + "(Room id: " + room.getRoomID() + ")",
                "", // Blank line
                ChatColor.WHITE + "Map Details: ",
                ChatColor.GRAY + "Map name: " + ChatColor.GREEN + room.getMapName(),
                ChatColor.GRAY + "Mode/ Difficultly: " + ChatColor.GREEN + room.getDiff(),
                ChatColor.GRAY + "Public room: " + ChatColor.GREEN + room.isPublic()
        };
        meta.setLore(Arrays.asList(loreRoom));
        zombie.setItemMeta(meta);

        inventoryContents.set(1 + index * 2, 0, ClickableItem.empty(zombie));

        ItemMeta listMeta = new ItemStack(Material.BLACK_GLAZED_TERRACOTTA, 1).getItemMeta();
        listMeta.setDisplayName(ChatColor.GOLD + room.getMapName() + "'s players: ");
        List<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.WHITE + "Host: " + ChatColor.GOLD + room.getHost().player.getDisplayName());
        loreList.add(""); // Blank line
        loreList.add(ChatColor.WHITE + "Players list (" + room.getPlayers().size() +  "/" + room.getRoomCapacity() + "):");

        for (int playerIndex = 0; playerIndex < Math.min(room.getPlayers().size(), 10); playerIndex++) {
            ZombiesRoomUser cUser = room.getPlayers().get(playerIndex);
            if (cUser == room.getHost()) {
                loreList.add(ChatColor.GRAY + " ◼ " + ChatColor.GOLD + cUser.player.getName() + ChatColor.GREEN + " (host)");
            } else {
                loreList.add(ChatColor.GRAY + " ◼ " + cUser.player.getName());
            }
        }

        if (room.getPlayers().size() > 10) {
            loreList.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "And " + (room.getPlayers().size() - 10) + " more...");
            loreList.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Use /zombiesrooms pl " + room.getRoomID() + " to get all players");
        }

        listMeta.setLore(loreList);

        int verticalIndex = 2 + index * 2;
        if (room.getRoomCapacity() < 9) {

            for (int i = 0; i < room.getPlayers().size(); i++) {
                Material mat = room.getPlayers().get(i) == room.getHost() ? Material.ORANGE_TERRACOTTA : Material.GREEN_TERRACOTTA;
                ItemStack playerIndicator = new ItemStack(mat, 1);
                playerIndicator.setItemMeta(listMeta);

                inventoryContents.set(verticalIndex, i, ClickableItem.empty(playerIndicator));
            }

            for (int i = room.getPlayers().size(); i < room.getRoomCapacity(); i++) {
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
            player.sendMessage(ChatColor.GREEN + "Spectating room: " + room.getRoomName());
        }));

        inventoryContents.set(verticalIndex, 8, ClickableItem.of(joinVisual(room.getRoomName()), e -> {
            // TODO: Test code
            player.sendMessage(ChatColor.GREEN + "Joining room: " + room.getRoomName());
        }));
    }

    private ItemStack specVisual (String roomName) {
        ItemStack s = new ItemStack(Material.ENDER_EYE, 1);
        ItemMeta meta = s.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Spectate");
        String[] lore = new String[] {
                ChatColor.GRAY + "Click here to spectate ",
                ChatColor.GOLD + roomName + ChatColor.GRAY + ". If the room is",
                ChatColor.GRAY + "private, we will send a request to the",
                ChatColor.GRAY + "room host"
        };

        meta.setLore(Arrays.asList(lore));
        s.setItemMeta(meta);

        return s;
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
        ItemStack s = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = s.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "No room");
        String[] lore = new String[] {
                ChatColor.GRAY + "Nothing to show here, you can",
                ChatColor.GRAY + "try to create your own room"
        };

        meta.setLore(Arrays.asList(lore));
        s.setItemMeta(meta);

        return s;
    }

    private ItemStack previousPageVisual (int currentPage) {
        return getItemStack(currentPage - 1, "Previous page");
    }

    private ItemStack nextPageVisual (int currentPage) {
        return getItemStack(currentPage + 1, "Next page");
    }

    private ItemStack filterVisual () {
        ItemStack filter = new ItemStack(Material.HOPPER, 1);
        ItemMeta meta = filter.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Filters...");
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

        meta.setLore(Arrays.asList(lore));
        filter.setItemMeta(meta);

        return filter;
    }

    private ItemStack createRoomVisual () {
        ItemStack item = new ItemStack(Material.MAP, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Create room");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Click to create a new room!"));
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack searchVisual() {
        ItemStack item = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Search");
        List<String> lore = new ArrayList<>();
        if(user.search != null) {
            lore.add(ChatColor.WHITE + "Search for: " + ChatColor.AQUA + user.search);
        }
        lore.add(""); // Blank line
        lore.add(ChatColor.GRAY + "Click here to enter text");
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getItemStack(int page, String s) {
        ItemStack pre = new ItemStack(Material.ARROW, Math.min(user.currentPage, 64));
        ItemMeta meta = pre.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + s);
        String[] lores = new String[] {
                ChatColor.GRAY + "Click to navigate to page " ,
                "" + ChatColor.GOLD + page
        };

        meta.setLore(Arrays.asList(lores));
        pre.setItemMeta(meta);

        return pre;
    }
}
