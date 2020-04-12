package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.commands.mapeditor.ContextManager;
import io.github.zap.zombiesplugin.commands.mapeditor.IEditorContext;
import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.map.round.Round;
import io.github.zap.zombiesplugin.map.spawn.SpawnFilter;
import io.github.zap.zombiesplugin.utils.Tuple;

import java.util.ArrayList;
import java.util.Hashtable;

public class GameMapData implements IMapData<GameMap>, IEditorContext {
    public String name;
    public ArrayList<SpawnFilterData> spawnFilters;
    public ArrayList<RoundData> rounds;
    public Hashtable<String, RoomData> rooms;

    public GameMapData() { }

    public GameMapData(GameMap from) {
        name = from.getName();
        spawnFilters = new ArrayList<>();
        rounds = new ArrayList<>();
        rooms = new Hashtable<>();

        for(SpawnFilter filter : from.getSpawnFilters()) {
            spawnFilters.add(new SpawnFilterData(filter));
        }

        for(Round round : from.getRounds()) {
            rounds.add(new RoundData(round));
        }

        for(Room room : from.getRooms()) {
            rooms.put(from.getName(), new RoomData(room, this));
        }
    }

    public RoomData getRoom(String name) {
        return rooms.getOrDefault(name,  null);
    }

    @Override
    public GameMap load(Object args) {
        GameMap result = new GameMap(name);

        for(SpawnFilterData data : spawnFilters) {
            result.add(data.load(null));
        }

        for(RoundData data : rounds) {
            result.add(data.load(null));
        }

        for(RoomData data : rooms.values()) {
            result.add(data.load(result));
        }

        return result;
    }

    @Override
    public Tuple<Boolean, String> canExecute(ContextManager session, String commandName, String[] args) {
        switch(commandName) {
            case "spawnfilter":
                if(args.length == 1) {
                    switch(args[0]) {
                        case "create":
                            return new Tuple<>(true, "Created new spawnfilter.");
                        default:
                            return new Tuple<>(false, "Usage: /spawnfilter create");
                    }
                }
                return new Tuple<>(false, "Usage: /spawnfilter create");
            default:
                return new Tuple<>(false, "That command cannot be run on a spawnfilter.");
        }
    }

    @Override
    public void execute(ContextManager session, String name, String[] args) {
        if(name.equals("spawnfilter")) {
            SpawnFilterData data = new SpawnFilterData();
            session.setFilter(data);
            spawnFilters.add(new SpawnFilterData());
        }
    }
}
