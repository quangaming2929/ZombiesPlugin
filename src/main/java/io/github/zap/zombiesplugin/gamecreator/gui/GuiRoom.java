package io.github.zap.zombiesplugin.gamecreator.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.data.IProvideDescription;
import io.github.zap.zombiesplugin.gamecreator.GameRoom;
import io.github.zap.zombiesplugin.gamecreator.ZombiesRoomUser;
import io.github.zap.zombiesplugin.manager.GameDifficulty;
import io.github.zap.zombiesplugin.manager.GameState;
import io.github.zap.zombiesplugin.map.GameMap;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import static io.github.zap.zombiesplugin.gamecreator.gui.CommonVisual.*;

public class GuiRoom implements InventoryProvider {
    private static final int PLAYER_LIST_DISPLAY_COUNT = 4;
    private static final int UPDATE_INTERVAL = 20;
    private static final int MAP_UPDATE_INTERVAL = 10000; // after this interval, every new room created will update the map list

    private long lastUpdatedMap = 0;

    protected static final List<GameMap> availableMaps = new ArrayList<>(); // ArrayList can be accessed by index

    protected final GameRoom room;
    protected final ZombiesRoomUser user;


    public GuiRoom(GameRoom room, ZombiesRoomUser user) {
        this.room = room;
        this.user = user;

        collectMaps();
    }

    // We only allow update map after a certain amount of time when a room is created
    private void collectMaps() {
        if (System.currentTimeMillis() + MAP_UPDATE_INTERVAL > lastUpdatedMap) {
            availableMaps.clear();
            availableMaps.addAll(ZombiesPlugin.instance.getMaps().values());
            lastUpdatedMap = System.currentTimeMillis();
        }
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        inventoryContents.set(0, 8, ClickableItem.of(leaveRoomVisual(), e -> {
            System.out.println("z"); // TODO: Test code
            ZombiesPlugin.instance.getRoomManager().leaveRoomClick(user);
        }));
        inventoryContents.set(5, 8, ClickableItem.of(enterArenaVisual(), e -> {
            player.sendMessage("Enter arena");
        }));

        updatePage(player, inventoryContents);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
        // Measure the tick for this inventory
        int tick = inventoryContents.property("tick", 0);
        inventoryContents.setProperty("tick", tick += 1);

        if (tick % UPDATE_INTERVAL == 0) {
            updatePage(player, inventoryContents);
        }
    }

    private void updatePage(Player player, InventoryContents inventoryContents) {
        inventoryContents.set(0, 0, ClickableItem.empty(infoVisual()));
        inventoryContents.set(5, 0, ClickableItem.of(readyStateVisual(user.isReady), e -> {
            if (user.currentRoom != null) {
                if (user.isReady) {
                    room.notReady(user);
                } else {
                    room.ready(user);
                }
            }
        }));


        if (user == room.getHost()) {
            inventoryContents.set(0, 1, ClickableItem.of(changeNameVisual(), e-> changeNameClick()));
            inventoryContents.set(0, 2, ClickableItem.of(changePasswordVisual(), e -> changePasswordClick()));
            inventoryContents.set(0, 3, ClickableItem.of(roomCapacityVisual(), e -> changeRoomCapacityClick()));

            // Note: always check inside the event as well because the update is not instant.
            //       People can spam and glitch it
            inventoryContents.set(2, 0, ClickableItem.of(previousPageVisual(room.getCurrentMapPage()), e -> {
                if(user == room.getHost()) {
                    room.setCurrentMapPage(room.getCurrentMapPage() - 1);
                    updatePage(player, inventoryContents);
                } else {
                    player.sendMessage(ChatColor.RED + "You need to be host to change this setting!");
                }
            }));

            inventoryContents.set(2, 4, ClickableItem.of(nextPageVisual(room.getCurrentMapPage()), e -> {
                if(user == room.getHost()) {
                    room.setCurrentMapPage(room.getCurrentMapPage() + 1);
                    updatePage(player, inventoryContents);
                } else {
                    player.sendMessage(ChatColor.RED + "You need to be host to change this setting!");
                }
            }));

            updateMapSelector(player, inventoryContents);

            inventoryContents.set(3, 0, ClickableItem.of(previousPageVisual(room.getCurrentModePage()), e -> {
                room.setCurrentModePage(room.getCurrentModePage() - 1);
                updatePage(player, inventoryContents);
            }));

            inventoryContents.set(3, 4, ClickableItem.of(nextPageVisual(room.getCurrentModePage()), e -> {
                room.setCurrentModePage(room.getCurrentModePage() + 1);
                updatePage(player, inventoryContents);
            }));

            updateModeSelector(player, inventoryContents);


            updateGameControllerVisual(inventoryContents);
        } else { // Remove all controls for host
            inventoryContents.set(0, 1, ClickableItem.empty(null));
            inventoryContents.set(0, 2, ClickableItem.empty(null));
            inventoryContents.set(5, 7, ClickableItem.empty(null));

            fillRegion(2, 0, 3 , 4, ClickableItem.empty(null), inventoryContents);
        }

        updatePlayerList(inventoryContents);
    }

    private void updateMapSelector (Player player, InventoryContents contents) {
        fillRegion(2, 1, 2, 3, ClickableItem.empty(null), contents);

        int logicPage = room.getCurrentMapPage() - 1;
        int firstIndex = logicPage * 3;

        if (availableMaps.size() > firstIndex) {
            for (int i = 0; i < 3; i++) {
                if (availableMaps.size() > firstIndex + i) {
                    ItemStack map = displayDescriptionProvider(availableMaps.get(firstIndex + i));
                    int mapIndex = firstIndex + i;
                    contents.set(2, 1 + i, ClickableItem.of(map, e -> {
                        if(user == room.getHost()) {
                            room.setSelectedMap(availableMaps.get(mapIndex));
                            updatePage(player, contents);
                        } else  {
                            player.sendMessage(ChatColor.RED + "You need to be host to change this setting!");
                        }
                    }));
                } else {
                    break;
                }
            }
        } else {
            ItemStack nothing = showNothingVisual();
            contents.set(2, 2, ClickableItem.empty(nothing));
        }
    }

    /**
     * Pass null here to show debug information and ask user to
     * submit bug. Although the chance of error is small
     */
    private ItemStack displayDescriptionProvider(IProvideDescription description) {
        ItemStack i = null;

        if (description != null) {
            i = description.getDescriptionVisual();
        } else {
            i = createVisualItem(
                    Material.ZOMBIE_HEAD,
                    1,
                    ChatColor.GREEN + "No description :(",
                    Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Please report this to the admins",
                                  ChatColor.GRAY + "\"No description\"")
            );
        }

        if (room.getSelectedMap() == description || room.getSelectedDiff() == description) {
            glowItemStack(i);
        }

        return i;
    }

    private void updateModeSelector (Player player, InventoryContents contents) {
        fillRegion(3, 1, 3, 3, ClickableItem.empty(null), contents);

        if (room.getSelectedMap() != null) {
            List<GameDifficulty> availableDiffs = room.getSelectedMap().getAcceptedDifficulty();
            if (availableDiffs != null && availableDiffs.size() == 0) { // Empty this is unexpected
                ItemStack noDiffError = createVisualItem(
                        Material.BARRIER,
                        1,
                        ChatColor.RED + "Err, this is embarrassing",
                        Arrays.asList(ChatColor.GRAY + "This is unexpected :(. Please report",
                                ChatColor.GRAY + "to the admin \"No difficulty accepted. \""));
                String msg = "No difficulty accepted. (Map: " + room.getSelectedMap().getName() +
                        ", RoomID: " + room.getRoomID() + ", Host: " + room.getHost().player.getDisplayName();
                ZombiesPlugin.instance.getLogger().log(Level.WARNING, msg);
            } else {
                int logicPage = room.getCurrentModePage() - 1;
                int firstIndex = logicPage * 3;

                if (availableDiffs.size() > firstIndex) {
                    for (int i = 0; i < 3; i++) {
                        if (availableDiffs.size() > firstIndex + i) {
                            ItemStack mode = displayDescriptionProvider(availableDiffs.get(firstIndex + i));
                            int modeIndex = firstIndex + i;
                            contents.set(3, 1 + i, ClickableItem.of(mode, e -> {
                                if(user == room.getHost()) {
                                    room.setSelectedDiff(availableDiffs.get(modeIndex));
                                    updatePage(player, contents);
                                } else {
                                    player.sendMessage(ChatColor.RED + "You need to be host to change this setting!");
                                }
                            }));
                        } else {
                            break;
                        }
                    }
                } else {
                    ItemStack nothing = showNothingVisual();
                    contents.set(3, 2, ClickableItem.empty(nothing));
                }
            }
        } else {
            ItemStack selectMap = createVisualItem(
                    Material.BARRIER,
                    1,
                    ChatColor.RED + "No map selected!",
                    Arrays.asList(ChatColor.GRAY + "Please select a map, mode/difficulty",
                                  ChatColor.GRAY + "will appear here."));
            contents.set(3, 2, ClickableItem.empty(selectMap));
        }
    }

    private ItemStack showNothingVisual() {
        return createVisualItem(
                Material.BARRIER,
                1,
                ChatColor.RED + "Nothing to show",
                Arrays.asList(ChatColor.GRAY + "Try to click navigate back button."));
    }

    private void changeNameClick () {
        ItemStack i = createVisualItem(
                Material.NAME_TAG,
                1,
                "",
                Arrays.asList(ChatColor.GRAY + "Please enter a new room name."));

        new AnvilGUI.Builder()
                .onComplete((p,s) -> {
                    room.setRoomName(s);
                    ZombiesPlugin.instance.getRoomManager().openGUI(user.player);
                    return AnvilGUI.Response.close();
                })
                .onClose(p -> ZombiesPlugin.instance.getRoomManager().openGUI(user.player))
                .title("Change room name")
                .text("Please enter a new name here!")
                .plugin(ZombiesPlugin.instance)
                .item(i)
                .open(user.player);
    }

    private void changePasswordClick () {
        String[] lore = new String[] {
                ChatColor.GRAY + "Please enter a new room password.",
                "",
                ChatColor.GREEN + "Change password to " + ChatColor.AQUA + "Empty \"\" (without ",
                ChatColor.AQUA + "quotes)" + ChatColor.GRAY + " to set the room type",
                ChatColor.GRAY + "to public."
        };

        ItemStack i = createVisualItem(
                Material.END_ROD,
                1,
                "",
                Arrays.asList(lore));

        new AnvilGUI.Builder()
                .onComplete((p,s) -> {
                    room.setRoomPassword(s);
                    ZombiesPlugin.instance.getRoomManager().openGUI(user.player);
                    return AnvilGUI.Response.close();
                })
                .onClose(p -> ZombiesPlugin.instance.getRoomManager().openGUI(user.player))
                .title("Change room name")
                .text("Please enter a new name here!")
                .plugin(ZombiesPlugin.instance)
                .item(i)
                .open(user.player);
    }

    private void changeRoomCapacityClick() {
        String[] lore = new String[] {
                ChatColor.GRAY + "Please enter a number to set a",
                ChatColor.GRAY + "new room capacity",
                "", // Blank line
                ChatColor.GRAY + "Note: please type a whole number",
                ChatColor.GRAY + "from " + ChatColor.AQUA + "1" + ChatColor.GRAY + " - " + ChatColor.AQUA + "100" + ChatColor.GRAY + ". If the room",
                ChatColor.GRAY + "has more players than the capacity,",
                ChatColor.GRAY + "they will be " + ChatColor.RED + "kicked" + ChatColor.GRAY + "."
        };

        ItemStack i = createVisualItem(
                Material.CHEST,
                1,
                "",
                Arrays.asList(lore));

        new AnvilGUI.Builder()
                .onComplete((p,s) -> {
                    // Data validation
                    if (StringUtils.isNumeric(s)) {
                        Integer input = Integer.parseInt(s);
                        if (input > 0 && input < 101) {
                            room.setRoomCapacity(input);
                            ZombiesPlugin.instance.getRoomManager().openGUI(user.player);
                            return AnvilGUI.Response.close();
                        }
                    }

                    return AnvilGUI.Response.text("Please enter a valid numbers");
                })
                .onClose(p -> ZombiesPlugin.instance.getRoomManager().openGUI(user.player))
                .title("Change room name")
                .text("Please enter a new name here!")
                .plugin(ZombiesPlugin.instance)
                .item(i)
                .open(user.player);
    }

    private void updateGameControllerVisual(InventoryContents inventoryContents) {
        if (room.getGameState() == GameState.PREGAME) {
            if (room.isHostReady()) {
                if (room.isAllReady()) {
                    inventoryContents.set(5, 7, ClickableItem.of(startGameVisual(), e -> {
                        // TODO: Placeholder action
                        ZombiesPlugin.instance.getServer().broadcastMessage("line updateGameControllerVisual() GuiRoom.java is not implemented");
                    }));
                } else {
                    inventoryContents.set(5, 7, ClickableItem.of(forceStartGameVisual(), e -> {
                        ZombiesPlugin.instance.getServer().broadcastMessage("line updateGameControllerVisual() GuiRoom.java is not implemented");
                    }));
                }
            } else {
                inventoryContents.set(5, 7, ClickableItem.empty(null));
            }
        } else if (room.getGameState() == GameState.COUNTDOWN) { // Show cancel game
            inventoryContents.set(5, 7, ClickableItem.of(cancelGameVisual(), e -> {
                ZombiesPlugin.instance.getServer().broadcastMessage("line updateGameControllerVisual() GuiRoom.java is not implemented");
            }));
        } else if (room.getGameState() == GameState.STARTED) {
            inventoryContents.set(5, 7, ClickableItem.of(abortGameVisual(), e -> {
                ZombiesPlugin.instance.getServer().broadcastMessage("line updateGameControllerVisual() GuiRoom.java is not implemented");
            }));
        }
    }

    private void updatePlayerList (InventoryContents inventoryContents) {
        // Create Meta
        String displayName = ChatColor.GOLD + room.getRoomName() + "'s players: ";
        List<String> playerListLore = new ArrayList<>();
        playerListLore.add(ChatColor.WHITE + "Host: " + ChatColor.GOLD + room.getHost().player.getDisplayName());
        playerListLore.add("");
        playerListLore.add(ChatColor.WHITE + "Players list (" + room.getPlayerCount() + "/" + room.getRoomCapacity() + "):");

        playerListLore.add(ChatColor.YELLOW + "Not ready players: " + ChatColor.WHITE + "(" + (room.getPlayerCount() - room.getReadyCount()) + "/" + room.getPlayerCount() + "):");
        for (int i = 0; i < Math.min(room.getNotReadyUsers().size(), PLAYER_LIST_DISPLAY_COUNT); i++) {
            playerListLore.add(getDisplayNameInList(room.getNotReadyUsers().get(i)));
        }
        if (room.getNotReadyUsers().size() > PLAYER_LIST_DISPLAY_COUNT) {
            playerListLore.add(ChatColor.GRAY + "And " + (room.getNotReadyUsers().size() - PLAYER_LIST_DISPLAY_COUNT) + " more...");
        }

        playerListLore.add(ChatColor.GREEN + "Ready players: " + ChatColor.WHITE + "(" + room.getReadyUsers().size() + "/" + room.getPlayerCount() + "):");
        for (int i = 0; i < Math.min(room.getReadyUsers().size(), PLAYER_LIST_DISPLAY_COUNT); i++) {
            playerListLore.add(getDisplayNameInList(room.getReadyUsers().get(i)));
        }
        if (room.getReadyUsers().size() > PLAYER_LIST_DISPLAY_COUNT) {
            playerListLore.add(ChatColor.GRAY + "And " + (room.getReadyUsers().size() - PLAYER_LIST_DISPLAY_COUNT) + " more...");
        }

        playerListLore.add(""); // Blank line
        playerListLore.add(ChatColor.GRAY + "Spectators: (" + room.getSpectators().size() + "):");
        for (int i = 0; i < Math.min(room.getSpectators().size(), PLAYER_LIST_DISPLAY_COUNT); i++) {
            playerListLore.add(getDisplayNameInList(room.getSpectators().get(i)));
        }
        if (room.getSpectators().size() > PLAYER_LIST_DISPLAY_COUNT) {
            playerListLore.add(ChatColor.GRAY + "And " + (room.getSpectators().size() - PLAYER_LIST_DISPLAY_COUNT) + " more...");
        }

        playerListLore.add("");
        playerListLore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Use /zombiesrooms pl " + room.getRoomID() + " to get all players");

        // Layouts
        if (room.getRoomCapacity() > 9) { // Mega room visual
            ItemStack megaRoomItemStack = createVisualItem(
                    Material.PINK_TERRACOTTA,
                    Math.min(room.getPlayerCount(), 64),
                    displayName,
                    playerListLore);

            inventoryContents.fillRow(1, ClickableItem.empty(megaRoomItemStack));
        } else { // Normal room visual
            for (int i = 0; i < room.getPlayerCount(); i++) {
                ZombiesRoomUser u = room.getPlayers().get(i);
                Material mat = u.isReady ? Material.GREEN_TERRACOTTA : Material.ORANGE_TERRACOTTA;
                ItemStack normalRoomItemStack = createVisualItem(mat, 1, displayName, playerListLore);
                inventoryContents.set(1, i, ClickableItem.empty(normalRoomItemStack));
            }

            for (int i = room.getPlayerCount(); i < room.getRoomCapacity(); i++) {
                ItemStack emptyItemStack = createVisualItem(Material.WHITE_TERRACOTTA, 1, displayName, playerListLore);
                inventoryContents.set(1, i, ClickableItem.empty(emptyItemStack));
            }
        }
    }

    private String getDisplayNameInList (ZombiesRoomUser u) {
        if (room.getHost() == u) {
            return ChatColor.GOLD + " ◼ " + u.player.getDisplayName() + ChatColor.GREEN + " (Host)";
        } else {
            return ChatColor.WHITE + " ◼ " + u.player.getDisplayName();
        }
    }

    public ItemStack infoVisual() {
        return createVisualItem(
                Material.ZOMBIE_HEAD,
                1,
                ChatColor.GREEN + "Show information for " + room.getRoomName() + ":",
                Arrays.asList(getRoomDetailLore(room)));
    }

    public ItemStack changeNameVisual () {
        return createVisualItem(
                Material.NAME_TAG,
                1,
                ChatColor.GREEN + "Change room name",
                Arrays.asList(ChatColor.GRAY + "Click to change the current room's name"));
    }

    public ItemStack changePasswordVisual() {
        String[] lore = new String[] {
                ChatColor.GRAY + "Click to change the current room's password!",
                "",
                ChatColor.GREEN + "Change password to " + ChatColor.AQUA + "Empty \"\" (without ",
                ChatColor.AQUA + "quotes)" + ChatColor.GRAY + " to set the room type",
                ChatColor.GRAY + "to public."
        };

        return createVisualItem(
                Material.END_ROD,
                1,
                ChatColor.RED + "Change password",
                Arrays.asList(lore));
    }

    public ItemStack leaveRoomVisual () {
        return createVisualItem(
                Material.OAK_DOOR,
                1,
                ChatColor.RED + "Leave room",
                Arrays.asList(ChatColor.GRAY + "Click to leave this room."));
    }

    public ItemStack readyStateVisual(boolean isReady) {
        Material mat = Material.CREEPER_SPAWN_EGG;
        String displayName = ChatColor.GREEN + "You are ready!";
        List<String> lore = new ArrayList<>();

        if (isReady) {
            lore.add(ChatColor.WHITE + "Click to not ready!");
        } else {
            mat = Material.WOLF_SPAWN_EGG;
            displayName = ChatColor.WHITE + "You are not ready!";
            lore.add(ChatColor.GREEN + "Click to ready!");
        }

        return createVisualItem(mat, 1, displayName, lore);
    }

    public ItemStack enterArenaVisual() {
        return createVisualItem(
                Material.ZOMBIE_SPAWN_EGG,
                1,
                ChatColor.GREEN + "Enter arena!",
                Arrays.asList(ChatColor.GRAY + "Click to enter the map!"));
    }

    public ItemStack forceStartGameVisual () {
        String[] lore = new String[] {
                ChatColor.GRAY + "Not all players are ready to play,",
                ChatColor.GRAY + "tell them to ready or force start",
                ChatColor.GRAY + "will " + ChatColor.RED + "kick" + ChatColor.GRAY + " not ready players",
                ChatColor.GRAY + "and start the game!"
        };

        return createVisualItem(
                Material.RED_WOOL,
                1,
                ChatColor.RED + "Force start game!",
                Arrays.asList(lore));
    }

    public ItemStack startGameVisual() {
        String[] lore = new String[] {
                ChatColor.GRAY + "Wrap players into arena and",
                ChatColor.GRAY + "start the game!"
        };

        return createVisualItem(
                Material.GREEN_WOOL,
                1,
                ChatColor.GREEN + "Start game!",
                Arrays.asList(lore));
    }

    public ItemStack cancelGameVisual() {
        return createVisualItem(
                Material.ORANGE_WOOL,
                1,
                ChatColor.GOLD + "Cancel game!",
                Arrays.asList(ChatColor.GRAY + "Click to cancel the game!"));
    }

    public ItemStack abortGameVisual() {
        return createVisualItem(
                Material.RED_WOOL,
                1,
                ChatColor.RED + "Abort game!",
                Arrays.asList(ChatColor.GRAY + "Click to abort the game, all player", ChatColor.GRAY + "will return to lobby."));
    }

    public ItemStack roomCapacityVisual() {
        return createVisualItem(
                Material.CHEST,
                1,
                ChatColor.GREEN + "Room capacity",
                Arrays.asList(ChatColor.GRAY + "Click to change room player",
                              ChatColor.GRAY + "capacity."));
    }
}
