package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.map.round.Round;
import io.github.zap.zombiesplugin.map.spawn.SpawnFilter;
import org.bukkit.Material;

import java.util.ArrayList;

public class GameMapData implements IMapData<GameMap> {
    public String name;
    public ArrayList<SpawnFilterData> spawnFilters;
    public ArrayList<RoundData> rounds;
    public ArrayList<RoomData> rooms;

    public GameMapData() { }

    public GameMapData(GameMap from) {
        name = from.getName();
        spawnFilters = new ArrayList<>();
        rounds = new ArrayList<>();
        rooms = new ArrayList<>();

        for(SpawnFilter filter : from.getSpawnFilters()) {
            spawnFilters.add(new SpawnFilterData(filter));
        }

        for(Round round : from.getRounds()) {
            rounds.add(new RoundData(round));
        }

        for(Room room : from.getRooms()) {
            rooms.add(new RoomData(room));
        }
    }

    @Override
    public GameMap load(Object args) {
        // TODO: @John-DND: Request review, I don't know how you serialize the map config
        //       I just add placeholder values here
        GameMap result = new GameMap("z", name, new ArrayList<>(), Material.ZOMBIE_HEAD, new ArrayList<>());

        for(SpawnFilterData data : spawnFilters) {
            result.add(data.load(null));
        }

        for(RoundData data : rounds) {
            result.add(data.load(null));
        }

        for(RoomData data : rooms) {
            result.add(data.load(result));
        }

        return result;
    }
}
