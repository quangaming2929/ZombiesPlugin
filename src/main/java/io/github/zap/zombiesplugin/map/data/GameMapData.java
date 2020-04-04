package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.map.round.Round;
import io.github.zap.zombiesplugin.map.spawn.SpawnFilter;

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
    public GameMap load() {
        GameMap result = new GameMap(name);

        for(SpawnFilterData data : spawnFilters) {
            result.add(data.load());
        }

        for(RoundData data : rounds) {
            result.add(data.load());
        }

        for(RoomData data : rooms) {
            result.add(data.load());
        }

        return result;
    }
}
