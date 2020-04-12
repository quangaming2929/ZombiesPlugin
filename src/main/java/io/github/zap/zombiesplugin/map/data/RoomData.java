package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.commands.mapeditor.ContextManager;
import io.github.zap.zombiesplugin.commands.mapeditor.IEditorContext;
import io.github.zap.zombiesplugin.map.*;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.github.zap.zombiesplugin.shop.Shop;
import io.github.zap.zombiesplugin.utils.MathUtils;
import io.github.zap.zombiesplugin.utils.Tuple;

import java.util.ArrayList;

public class RoomData implements IMapData<Room>, IEditorContext {
    public String name;
    public ArrayList<DoorData> doors;
    public ArrayList<WindowData> windows;
    public ArrayList<SpawnPointData> spawnPoints;
    public ArrayList<ShopData> shops;
    public MultiBoundingBoxData bounds;

    private transient GameMapData parent;

    public RoomData() {}

    public RoomData(Room from, GameMapData parent) {
        this.parent = parent;
        name = from.getName();
        doors = new ArrayList<>();
        windows = new ArrayList<>();
        spawnPoints = new ArrayList<>();
        shops = new ArrayList<>();

        for(Door door : from.getDoors()) {
            doors.add(new DoorData(door));
        }

        for(Window window : from.getWindows()) {
            windows.add(new WindowData(window, this));
        }

        for(SpawnPoint spawnPoint : from.getSpawnpoints()) {
            spawnPoints.add(new SpawnPointData(spawnPoint, this));
        }

        for(Shop shop : from.getShops()) {
            shops.add(new ShopData(shop));
        }

        bounds = new MultiBoundingBoxData(from.getBounds());
    }

    @Override
    public Room load(Object args) {
        if(args instanceof GameMap) {
            GameMap map = (GameMap)args;
            Room result = new Room(name, bounds.load(null), map);

            for(DoorData data : doors) {
                result.add(data.load(result));
            }

            for(WindowData data : windows) {
                result.add(data.load(result));
            }

            for(SpawnPointData data : spawnPoints) {
                result.add(data.load(result));
            }

            for(ShopData data : shops) {
                result.add(data.load(result));
            }

            return result;
        }
        else {
            throw new IllegalArgumentException("args must be an instance of GameMap");
        }
    }

    @Override
    public Tuple<Boolean, String> canExecute(ContextManager session, String commandName, String[] args) {
        switch(commandName) {
            case "window":
                if(args.length == 1) {
                    switch(args[0].toLowerCase()) {
                        case "create":
                            if(session.hasBounds()) {
                                return new Tuple<>(true, "Window created.");
                            }
                            return new Tuple<>(false, "This command requires a bound to be defined.");
                        case "delete":
                            if(windows != null && windows.size() > 0) {
                                return new Tuple<>(true, "Deleted window.");
                            }
                            return new Tuple<>(false, "There are no windows to delete.");
                        default:
                            return new Tuple<>(false, "Usage: /window create or /window delete");
                    }
                }
                return new Tuple<>(false, "Usage: /window create or /window delete");
            case "spawn":
                if(args.length == 0) {
                    return new Tuple<>(true, "Added spawnpoint.");
                }
                return new Tuple<>(false, "Usage: /spawn");
            case "bounds":
                if(args.length == 0) {
                    if(session.hasBounds()) {
                        return new Tuple<>(true, "Added bounds.");
                    }
                    return new Tuple<>(false, "This command requires a bound to be defined.");
                }
                else if(args.length == 2) {
                    String action = args[0].toLowerCase();
                    String index = args[1].toLowerCase();
                    if (action.equals("delete")) {
                        Tuple<Boolean, Integer> parse = MathUtils.tryParseInt(index, 0);
                        if(parse.x) {
                            if(parse.y >= 0 && parse.y < windows.size()) {
                                return new Tuple<>(true, "Deleted bounds.");
                            }
                            return new Tuple<>(false, "Index out of bounds. There are currently "+ bounds.bounds.size() + " bounds in the current room.");
                        }
                        return new Tuple<>(false, "Usage: /bounds delete [index]");
                    }
                    return new Tuple<>(false, "Usage: /bounds delete [index]");
                }
                return new Tuple<>(false, "Usage: /bounds delete [index]");
            case "delete":
                if(args.length == 0) {
                    if(parent.rooms.getOrDefault(name, null) != null) {
                        return new Tuple<>(true, "Deleted the currently focused room.");
                    }
                    return new Tuple<>(false, "A room with that name does not exist.");
                }
                return new Tuple<>(false, "Usage: /delete");
            case "focus":
                if(args.length == 2) {
                    switch(args[0].toLowerCase()) {
                        case "window":
                            Tuple<Boolean, Integer> parse = MathUtils.tryParseInt(args[0], 0);
                            if(parse.x) {
                                if(parse.y >= 0 && parse.y < windows.size()) {
                                    return new Tuple<>(true, "Focused window.");
                                }
                                return new Tuple<>(false, "Index out of bounds. There are currently "+ windows.size() + " windows in the current room.");
                            }
                            return new Tuple<>(false, "Usage: /focus window [index]");
                        default:
                            return new Tuple<>(false, "Usage: /focus window [index]");
                    }
                }
                return new Tuple<>(false, "Usage: /focus window [index]");
            default:
                return new Tuple<>(false, "This command cannot be run on a room.");
        }
    }

    @Override
    public void execute(ContextManager session, String commandName, String[] args) {
        switch (commandName) {
            case "window":
                switch(args[0].toLowerCase()) {
                    case "create":
                        WindowData data = new WindowData();
                        data.windowBounds = new BoundingBoxData(new BoundingBox(session.getFirstLocation(), session.getSecondLocation()));
                        data.interiorBounds = new MultiBoundingBoxData();
                        data.coverMaterial = session.getFirstLocation().getBlock().getType().toString();

                        session.setFocus(data);
                        break;
                    case "delete":
                        windows.remove(windows.size() - 1);
                        break;
                }
                break;
            case "spawn":
                SpawnPointData spawnPointData = new SpawnPointData();
                spawnPointData.spawn = new LocationData(session.getLastLocation());
                break;
            case "bounds":
                if(args.length == 0) {
                    bounds.bounds.add(new BoundingBoxData(new BoundingBox(session.getFirstLocation(), session.getSecondLocation())));
                }
                else {
                    bounds.bounds.remove(Integer.parseInt(args[1]));
                }
                break;
            case "delete":
                parent.rooms.remove(name);
                session.setFocus(null);
                break;
            case "focus":
                switch (args[0].toLowerCase()) {
                    case "window":
                        session.setFocus(windows.get(Integer.parseInt(args[1])));
                        break;
                }
                break;
        }
    }
}