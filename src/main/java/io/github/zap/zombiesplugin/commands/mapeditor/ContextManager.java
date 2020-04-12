package io.github.zap.zombiesplugin.commands.mapeditor;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.data.GameMapData;
import io.github.zap.zombiesplugin.map.spawn.SpawnFilter;
import io.github.zap.zombiesplugin.utils.Tuple;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ContextManager implements Listener {
    private Player player;
    private IEditorContext context;
    private String currentMapName;

    private Location firstLocation;
    private Location secondLocation;

    private IEditorContext currentFilter;

    public ContextManager(Player player) {
        this.player = player;
        ZombiesPlugin.instance.getServer().getPluginManager().registerEvents(this, ZombiesPlugin.instance);
    }

    public Player getPlayer() {
        return player;
    }

    public String performCommand(String commandName, String[] args) {
        if(commandName.equals("mapeditor")) {
            if(args.length == 2) {
                String action = args[0];
                String name = args[1];
                switch(action) {
                    case "create":
                        if(!name.equals(currentMapName) && !ZombiesPlugin.instance.hasMap(name)) {
                            currentMapName = name;
                            context = new GameMapData(new GameMap(name));
                            return "Created new map '" + name + "'";
                        }
                        return "A map with that name already exists.";
                    case "focus":
                        if(name.equals(currentMapName)) {
                            return "You are already focused on this map.";
                        }
                        else {
                            GameMap existingMap = ZombiesPlugin.instance.getMap(name);
                            if(existingMap != null) {
                                currentMapName = name;
                                context = new GameMapData(existingMap);
                                return "Set focus to '" + name + "'.";
                            }
                            else {
                                return "That map does not exist.";
                            }
                        }
                    case "delete":
                        GameMap existingMap = ZombiesPlugin.instance.getMap(name);

                        if(name.equals(currentMapName) || existingMap != null) {
                            currentMapName = null;
                            context = null;

                            if(existingMap != null) {
                                ZombiesPlugin.instance.removeMap(name);
                            }
                            return "Map deleted.";
                        }
                        return "That map does not exist.";
                    default:
                        return "Usage: /mapeditor [create|focus|delete] map_name";
                }
            }
            else {
                return "Usage: /mapeditor [create|focus|delete] map_name";
            }
        }
        else {
            if(context != null) {
                Tuple<Boolean, String> result = context.canExecute(this, commandName, args);
                if(result.x) {
                    context.execute(this, commandName, args);
                }
                return result.y;
            }
            return "This command requires a context to run.";
        }
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getPlayer() == player) {
            Block clickedBlock = event.getClickedBlock();
            if(clickedBlock != null && clickedBlock.isPassable()) {
                Location clickedLocation = clickedBlock.getLocation();
                if(firstLocation == null) {
                    firstLocation = clickedLocation;
                }
                else {
                    secondLocation = firstLocation;
                    secondLocation = clickedLocation;
                }
            }
        }
    }

    public boolean setFocus(IEditorContext newContext) {
        if(newContext != context) {
            context = newContext;
            firstLocation = null;
            secondLocation = null;
            return true;
        }
        return false;
    }

    public void setFilter(IEditorContext context) {
        currentFilter = context;
    }

    public Location getFirstLocation() {
        return firstLocation;
    }

    public Location getSecondLocation() {
        return secondLocation;
    }

    public Location getLastLocation() {
        if(secondLocation == null) {
            return firstLocation;
        }
        return secondLocation;
    }

    public boolean hasBounds() {
        return firstLocation != null && secondLocation != null;
    }
}