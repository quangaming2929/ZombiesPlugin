package io.github.zap.zombiesplugin.commands;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.data.GameMapData;
import io.github.zap.zombiesplugin.map.data.LocationData;
import io.github.zap.zombiesplugin.map.data.RoomData;
import io.github.zap.zombiesplugin.map.data.SpawnPointData;
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
    private final Hashtable<UUID,EditorSession> editors;
    private final String editorLore = "Map Editor";

    public MapEditorCommands() {
        editors = new Hashtable<>();
        ZombiesPlugin.instance.getServer().getPluginManager().registerEvents(this, ZombiesPlugin.instance);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            Player player = (Player)sender;
            if(sender.isOp()) {
                UUID uuid = player.getUniqueId();
                String commandName = command.getName();
                EditorSession session = editors.getOrDefault(uuid, null);
                if(commandName.equals("mapeditor")) {
                    if(args.length == 1) {
                        String action = args[0].toLowerCase();
                        switch(action) {
                            case "save":
                                if(session != null) {
                                    GameMap map = session.getMapData().load(null);
                                    ZombiesPlugin.instance.removeMap(map.getName());
                                    ZombiesPlugin.instance.addMap(map);
                                    player.sendMessage("Map saved.");
                                }
                                else {
                                    player.sendMessage("There is no map currently in focus.");
                                }
                                break;
                            case "delete":
                                if(session != null) {
                                    editors.remove(uuid);
                                    player.sendMessage("Map deleted.");
                                }
                                else {
                                    player.sendMessage("There is no map currently in focus.");
                                }
                                break;
                            case "list":
                                if(session != null) {
                                    player.sendMessage("Current map: " + session.getMapData().name);
                                }

                                for(GameMap map : ZombiesPlugin.instance.getMaps().values()) {
                                    player.sendMessage(map.getName());
                                }
                                break;
                            default:
                                player.sendMessage("Usage: /mapeditor [save|delete|list] OR /mapeditor [edit|create] map_name");
                        }
                    }
                    else if(args.length == 2) {
                        String action = args[0].toLowerCase();
                        String name = args[1];
                        switch(action) {
                            case "focus":
                                GameMap target = ZombiesPlugin.instance.getMap(name);
                                if(target != null) {
                                    editors.remove(uuid);
                                    editors.put(uuid, new EditorSession(uuid, target));
                                    player.sendMessage("Set map focus.");
                                }
                                else {
                                    player.sendMessage("Map does not exist. Try running /map create");
                                }
                                break;
                            case "create":
                                editors.remove(uuid);
                                editors.put(uuid, new EditorSession(uuid, name));
                                break;
                            default:
                                player.sendMessage("Usage: /mapeditor [save|delete|list] OR /mapeditor [edit|create] map_name");
                        }
                    }
                    else {
                        player.sendMessage("Usage: /mapeditor [save|delete|list] OR /mapeditor [edit|create] map_name");
                    }
                }
                else if(commandName.equals("room")) {
                    if(session != null) {
                        if(args.length == 1) {
                            String action = args[0].toLowerCase();
                            switch(action) {
                                case "delete":
                                    RoomData data = session.getCurrentRoom();
                                    if(data != null) {
                                        session.getMapData().rooms.remove(data.name);
                                        player.sendMessage("Removed room.");
                                        session.setCurrentRoom(null);
                                    }
                                    else {
                                        player.sendMessage("This command requires a room to be in focus.");
                                    }
                                    break;
                                case "list":
                                    for(RoomData roomData : session.getMapData().rooms.values()) {
                                        player.sendMessage(roomData.name);
                                    }
                                    break;
                                default:
                                    player.sendMessage("Usage: /room [delete|list] OR /room [edit|create] room_name");
                            }
                        }
                        else if(args.length == 2) {
                            String action = args[0].toLowerCase();
                            String name = args[1];
                            switch(action) {
                                case "focus":
                                    RoomData room = session.getMapData().rooms.getOrDefault(name, null);
                                    if(room != null) {
                                        session.setCurrentRoom(room);
                                        player.sendMessage("'" + room.name + "' was focused.");
                                    }
                                    else {
                                        player.sendMessage("A room with that name does not exist.");
                                    }
                                    break;
                                case "create":
                                    if(!session.getMapData().rooms.containsKey(name)) {
                                        session.getMapData().rooms.put(name, new RoomData());
                                    }
                                    else {
                                        player.sendMessage("A room with that name already exists.");
                                    }
                                    break;
                                default:
                                    player.sendMessage("Usage: /room [save|delete|list] OR /room [edit|create] room_name");
                            }
                        }
                        else {
                            player.sendMessage("Usage: /room [save|delete|list] OR /room [edit|create] map_name");
                        }
                    }
                    else {
                        player.sendMessage("This command requires a map to be in focus. Try /map create or /map edit.");
                    }
                }
                else if(commandName.equals("roomspawn")) {
                    if(session != null) {
                        RoomData data = session.getCurrentRoom();
                        if(data != null) {
                            Location lastSelection = session.getLastSelection();
                            if(lastSelection != null) {
                                SpawnPointData spawnPointData = new SpawnPointData();
                                spawnPointData.spawn = new LocationData(session.getLastSelection());

                                data.spawnPoints.add(spawnPointData);
                            }
                            else {
                                player.sendMessage("You must select a location before using this command.");
                            }
                        }
                        else {
                            player.sendMessage("You must have a room focused to run this command.");
                        }
                    }
                    else {
                        player.sendMessage("You must have a map focused to run this command.");
                    }
                }
                else if(commandName.equals("windowblocks")) {
                    if(session != null) {
                        RoomData data = session.getCurrentRoom();
                        if(data != null) {
                            Location lastSelection = session.getLastSelection();
                            if(lastSelection != null) {
                                SpawnPointData spawnPointData = new SpawnPointData();
                                spawnPointData.spawn = new LocationData(session.getLastSelection());

                                data.spawnPoints.add(spawnPointData);
                            }
                            else {
                                player.sendMessage("You must select a location before using this command.");
                            }
                        }
                        else {
                            player.sendMessage("You must have a room focused to run this command.");
                        }
                    }
                    else {
                        player.sendMessage("You must have a map focused to run this command.");
                    }
                }
            }
            else {
                player.sendMessage("You do not have permission to execute this command.");
            }
        }
        return true;
    }

    @EventHandler
    private void onPlayerRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(player.isOp()) {
            UUID uuid = player.getUniqueId();
            EditorSession session = editors.getOrDefault(uuid, null);
            if(session != null) {
                Block clickedBlock = event.getClickedBlock();
                if(clickedBlock != null) {
                    ItemStack item = event.getItem();
                    if(item != null && item.getType() == Material.STICK) {
                        List<String> lore = item.getLore();
                        if(lore != null && lore.size() == 1 && lore.get(0).equals(editorLore)) {
                            session.setPreviousSelection(session.getLastSelection());
                            session.setLastSelection(clickedBlock.getLocation());
                        }
                    }
                }
            }
        }
    }

    private void giveItem(Player target, String lore) {
        ItemStack tool = new ItemStack(Material.STICK);

        ArrayList<String> list = new ArrayList<>();
        list.add(lore);
        tool.setLore(list);
        tool.addEnchantment(Enchantment.KNOCKBACK, 1);

        target.getInventory().setItemInMainHand(tool);
    }
}