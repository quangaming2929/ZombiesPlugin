package io.github.zap.zombiesplugin.commands;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.map.Window;
import io.github.zap.zombiesplugin.map.data.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MapEditorCommands implements CommandExecutor, Listener {
    private Hashtable<String, GameMapData> maps;
    private Hashtable<UUID,EditorSession> editors;

    private final HashSet<String> editorLore;

    public MapEditorCommands() {
        editors = new Hashtable<>();

        editorLore = new HashSet<>();
        editorLore.add("Window Editor");
        editorLore.add("Window Bounds Editor");
        editorLore.add("Room Bounds Editor");

        ZombiesPlugin.instance.getServer().getPluginManager().registerEvents(this, ZombiesPlugin.instance);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            Player player = (Player)sender;
            if(sender.isOp()) {
                String commandName = command.getName().toLowerCase();
                UUID id = player.getUniqueId();

                if(commandName.equals("mapeditor")) {
                    if(args.length == 1) {
                        String action = args[0].toLowerCase();

                        switch(action) {
                            case "list":
                                Collection<GameMap> all = ZombiesPlugin.instance.getMaps().values();
                                if(all.size() > 0) {
                                    StringBuilder message = new StringBuilder("Maps: ");
                                    int i = 0;
                                    for(GameMap map : all) {
                                        if(i == all.size() - 1) message.append(map.getName());
                                        else message.append(map.getName()).append(", ");
                                        i++;
                                    }

                                    player.sendMessage(message.toString());
                                }
                                else {
                                    player.sendMessage("There have been no maps created.");
                                }
                                return true;
                            case "current":
                                if(hasEditor(id)) {
                                    player.sendMessage("Current map: '" + editors.get(id) + "'");
                                }
                                else {
                                    player.sendMessage("There is no map currently in focus.");
                                }
                                return true;
                        }
                    }
                    else if(args.length == 2) {
                        String action = args[0].toLowerCase();
                        String name = args[1].replace('_', ' ');

                        switch (action) {
                            case "create":
                                if(!ZombiesPlugin.instance.hasMap(name)) {
                                    ZombiesPlugin.instance.addMap(new GameMap(name));

                                    editors.remove(id);
                                    editors.put(id, new EditorSession(id, name));
                                    player.sendMessage("Created map '" + name + "'.");
                                    return true;
                                }
                                else {
                                    player.sendMessage("A map named '" + name + "' already exists.");
                                }
                            case "edit":
                                if(ZombiesPlugin.instance.hasMap(name)) {
                                    editors.remove(id);
                                    editors.put(id, new EditorSession(id, name));

                                    player.sendMessage("Selected map '" + name + "'.");
                                    return true;
                                }
                                else {
                                    player.sendMessage("There is no map named '" + name + "'");
                                }
                                return true;
                            case "delete":
                                if(ZombiesPlugin.instance.hasMap(name)) {
                                    ZombiesPlugin.instance.removeMap(name);
                                    editors.remove(id);

                                    player.sendMessage("Deleted map '" + name + "'.");
                                }
                                else {
                                    player.sendMessage("There is no map named '" + name + "'");
                                }
                                break;
                            default:
                                player.sendMessage("Usage: /mapeditor [create|edit|delete] map_name OR /mapeditor [list|current]");
                                return true;
                        }
                    }
                    else {
                        player.sendMessage("Usage: /mapeditor [create|edit|delete] map_name OR /mapeditor [list|current]");
                    }
                }
                else if(hasEditor(id)) {
                    EditorSession session = editors.get(id);
                    GameMapData map = maps.get(session.getMapName());
                    RoomData room = session.getCurrentRoom();

                    if(commandName.equals("room")) {
                        if(args.length == 1) {
                            String action = args[0].toLowerCase();

                            switch(action) {
                                case "list":
                                    if(map.rooms.size() > 0) {
                                        for(RoomData sampleRoom : map.rooms.values()) {
                                            player.sendMessage(sampleRoom.name);
                                        }
                                    }
                                    else {
                                        player.sendMessage("There are no rooms belonging to the current map.");
                                    }
                                    return true;
                                case "current":
                                    if(session.getCurrentRoom() != null) {
                                        player.sendMessage(session.getCurrentRoom().name);
                                    }
                                    else {
                                        player.sendMessage("There is no room currently selected.");
                                    }
                                    return true;
                            }
                        }
                        else if(args.length == 2) {
                            String action = args[0].toLowerCase();
                            String name = args[1].replace('_', ' ');

                            switch (action) {
                                case "create":
                                    if(map.rooms.getOrDefault(name, null) == null) {
                                        RoomData newRoom = new RoomData();
                                        newRoom.name = name;
                                        map.rooms.put(name, newRoom);

                                        player.sendMessage("Added room '"+ name + "'");
                                        giveEditorTool(player, "Room Bounds Editor");
                                    }
                                    else {
                                        player.sendMessage("A room named '" + name + "' already exists on the current map.");
                                    }
                                    break;
                                case "edit":
                                    if(map.rooms.getOrDefault(name, null) != null) {
                                        session.setCurrentRoom(map.rooms.get(name));
                                        player.sendMessage("Selected room '" + name + "'.");
                                        giveEditorTool(player, "Room Bounds Editor");
                                    }
                                    break;
                                case "delete":
                                    if(map.rooms.getOrDefault(name, null) != null) {
                                        maps.remove(name);
                                        editors.remove(id);

                                        player.sendMessage("Removed room '" + name + "'.");
                                    }
                                    else {
                                        player.sendMessage("There is no room named '" + name + "'.");
                                    }
                                    break;
                                default:
                                    player.sendMessage("Usage: /room [create|edit|delete] room_name OR /room [list|current]");
                            }
                        }
                        else {
                            player.sendMessage("Usage: /room [create|edit|delete] room_name OR /room [list|current]");
                        }
                    }
                    else if(room != null) {
                        WindowData currentWindow = session.getCurrentWindow();

                        if(commandName.equals("roomspawn")) {
                            if(args.length == 0) {
                                giveEditorTool(player, "Room Spawnpoint Editor");
                            }
                            else if(args.length == 1) {
                                if(args[0].toLowerCase().equals("clear")) {
                                    room.spawnPoints.clear();
                                    player.sendMessage("Removed all in-room spawnpoints.");
                                }
                                else {
                                    player.sendMessage("Usage: /roomspawn OR /roomspawn clear");
                                }
                            }
                            else {
                                player.sendMessage("Usage: /roomspawn OR /roomspawn clear");
                            }
                        }
                        else if(currentWindow != null) {
                            if(commandName.equals("windowspawn")) {
                                if(args.length == 0) {
                                    giveEditorTool(player, "Window Spawnpoint Editor");
                                }
                                else {
                                    player.sendMessage("Usage: /windowspawn");
                                }
                            }
                            else if(commandName.equals("windowblocks")) {
                                if(args.length == 0) {
                                    giveEditorTool(player, "Window Block Editor");
                                }
                                else {
                                    player.sendMessage("Usage: /windowblocks");
                                }
                            }
                            else if(commandName.equals("windowinterior")) {
                                if(args.length == 0) {
                                    giveEditorTool(player, "Window Interior Editor");
                                }
                                else {
                                    player.sendMessage("Usage: /windowinterior");
                                }
                            }
                        }
                        else {
                            player.sendMessage("This command requires you to have previously created a window. Try running /window.");
                        }
                    }
                    else {
                        player.sendMessage("This command requires you to have a room selected. Try running /room create or /room edit.");
                    }
                }
                else {
                    player.sendMessage("This command requires you to have a map selected. Try running /mapeditor create or /mapeditor edit.");
                }
            }
            else {
                player.sendMessage("You do not have permission to execute this command.");
            }
        }
        return true;
    }

    private boolean hasEditor(UUID id) {
        return editors.containsKey(id);
    }

    private void giveEditorTool(Player target, String lore) {
        ItemStack tool = new ItemStack(Material.STICK);

        ArrayList<String> list = new ArrayList<>();
        list.add(lore);
        tool.setLore(list);
        tool.addEnchantment(Enchantment.KNOCKBACK, 69);

        target.getInventory().setItemInMainHand(tool);
    }

    @EventHandler
    private void onPlayerRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();

        if(hasEditor(id)) {
            EditorSession session = editors.get(id);
            ItemStack item = event.getItem();
            Block clickedBlock = event.getClickedBlock();

            if(clickedBlock != null) {
                Location currentLocation = clickedBlock.getLocation();
                if(item != null && item.getLore() != null) {
                    String name = item.getLore().size() == 0 ? null : item.getLore().get(0);
                    if(name != null) {
                        if(editorLore.contains(name)) {
                            if(name.equals("Room Spawnpoint Editor")) {
                                RoomData room = session.getCurrentRoom();
                                if(room != null) {
                                    SpawnPointData data = new SpawnPointData();
                                    data.spawn = new LocationData(currentLocation);
                                    room.spawnPoints.add(data);
                                }
                            }
                            else {
                                Location oldLocation = session.getLocation(name);
                                if(oldLocation == null) {
                                    session.putLocation(name, currentLocation);
                                }
                                else {
                                    WindowData window = session.getCurrentWindow();
                                    if(window != null) {
                                        BoundingBoxData data = new BoundingBoxData();
                                        data.origin = new LocationData(oldLocation);
                                        data.limit = new LocationData(currentLocation);

                                        switch(name) {
                                            case "Window Interior Editor":
                                                if(window.interiorBounds == null) {
                                                    window.interiorBounds = new MultiBoundingBoxData();
                                                }

                                                if(window.interiorBounds.bounds == null) {
                                                    window.interiorBounds.bounds = new ArrayList<>();
                                                }

                                                window.interiorBounds.bounds.add(data);

                                                player.sendMessage("Added region to bounds.");
                                                break;
                                            case "Window Spawnpoint Editor":
                                                window.spawnPoint = new SpawnPointData();
                                                window.spawnPoint.spawn = new LocationData(oldLocation);
                                                window.spawnPoint.target = new LocationData(currentLocation);

                                                player.sendMessage("Added spawnpoint and target.");
                                                break;
                                            case "Window Block Editor":
                                                window.windowBounds = data;
                                                player.sendMessage("Defined window blocks.");
                                                break;
                                        }
                                    }

                                    RoomData room = session.getCurrentRoom();
                                    if(room != null && name.equals("Room Bounds Editor")) {
                                        BoundingBoxData data = new BoundingBoxData();
                                        data.origin = new LocationData(oldLocation);
                                        data.limit = new LocationData(currentLocation);

                                        if(room.bounds == null) {
                                            room.bounds = new MultiBoundingBoxData();
                                        }

                                        if(room.bounds.bounds == null) {
                                            room.bounds.bounds = new ArrayList<>();
                                        }

                                        room.bounds.bounds.add(data);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}