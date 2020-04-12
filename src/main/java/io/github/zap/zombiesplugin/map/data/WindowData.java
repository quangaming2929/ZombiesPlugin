package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.commands.mapeditor.ContextManager;
import io.github.zap.zombiesplugin.commands.mapeditor.IEditorContext;
import io.github.zap.zombiesplugin.map.BoundingBox;
import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.map.Window;
import io.github.zap.zombiesplugin.utils.MathUtils;
import io.github.zap.zombiesplugin.utils.Tuple;
import org.bukkit.Material;

public class WindowData implements IMapData<Window>, IEditorContext {
    public SpawnPointData spawnPoint;
    public MultiBoundingBoxData interiorBounds;
    public BoundingBoxData windowBounds;
    public String coverMaterial;

    private transient RoomData parent;

    public WindowData() {}

    public WindowData(Window from, RoomData parent) {
        this.parent = parent;
        spawnPoint = new SpawnPointData(from.getSpawnpoint(0), this);
        interiorBounds = new MultiBoundingBoxData(from.getInteriorBounds());
        windowBounds = new BoundingBoxData(from.getWindowBounds());
        coverMaterial = from.getCoverMaterial().name();
    }

    @Override
    public Window load(Object args) {
        if(args instanceof Room) {
            Window window = new Window(windowBounds.load(null), interiorBounds.load(null), Material.getMaterial(coverMaterial), (Room)args);
            window.add(spawnPoint.load(window));
            return window;
        }
        else {
            throw new IllegalArgumentException("args must be a Room instance");
        }
    }

    @Override
    public Tuple<Boolean, String> canExecute(ContextManager session, String name, String[] args) {
        switch(name) {
            case "spawn":
                if(args.length == 0) {
                    if(session.hasBounds()) {
                        return new Tuple<>(true, "Set spawnpoint and target.");
                    }
                    return new Tuple<>(false, "This command requires two points to be selected.");
                }
                return new Tuple<>(false, "Usage: /spawn");
            case "bounds":
                if(args.length == 0) {
                    if(session.hasBounds()) {
                        return new Tuple<>(true, "Added new region to bounds.");
                    }
                    return new Tuple<>(false, "This command requires two points to be selected.");
                }
                else if(args.length == 2) {
                    String action = args[0].toLowerCase();
                    String index = args[1].toLowerCase();
                    if (action.equals("delete")) {
                        Tuple<Boolean, Integer> parse = MathUtils.tryParseInt(index, 0);
                        if(parse.x) {
                            if(parse.y >= 0 && parse.y < interiorBounds.bounds.size()) {
                                return new Tuple<>(true, "Deleted bounds.");
                            }
                            return new Tuple<>(false, "Index out of bounds. There are currently "+ interiorBounds.bounds.size() + " bounds in the current room.");
                        }
                        return new Tuple<>(false, "Usage: /bounds delete [index]");
                    }
                    return new Tuple<>(false, "Usage: /bounds delete [index]");
                }
                return new Tuple<>(false, "Usage: /bounds delete [index]");
            case "delete":
                if(args.length == 0) {
                    return new Tuple<>(true, "Removed the currently focused window.");
                }
                return new Tuple<>(false, "Usage: /delete");
            case "back":
                if(args.length == 0) {
                    return new Tuple<>(true, "Returned focus to room.");
                }
                return new Tuple<>(false, "Usage: /back");
            default:
                return new Tuple<>(false, "This command cannot be run on a window.");
        }
    }

    @Override
    public void execute(ContextManager session, String name, String[] args) {
        switch (name) {
            case "spawn":
                SpawnPointData data = new SpawnPointData();
                data.spawn = new LocationData(session.getFirstLocation());
                data.target = new LocationData(session.getSecondLocation());
                break;
            case "bounds":
                if(args.length == 0) {
                    interiorBounds.bounds.add(new BoundingBoxData(new BoundingBox(session.getFirstLocation(), session.getSecondLocation())));
                }
                else if(args.length == 2) {
                    interiorBounds.bounds.remove(Integer.parseInt(args[1]));
                }
                break;
            case "delete":
                parent.windows.remove(this);
                session.setFocus(null);
                break;
            case "back":
                session.setFocus(parent);
                break;
        }
    }
}
