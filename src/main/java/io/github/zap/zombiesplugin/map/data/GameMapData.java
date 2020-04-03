package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.round.Round;
import io.github.zap.zombiesplugin.map.spawn.SpawnFilter;

import java.util.ArrayList;

public class GameMapData {
    public String mapName;
    public ArrayList<String> spawnManagers;
    public ArrayList<RoundData> rounds;
    //TODO: make shops, doors, and rooms save themselves as well

    public GameMapData() { }

    public GameMapData(GameMap from) {
        mapName = from.name;
        spawnManagers = new ArrayList<>();
        rounds = new ArrayList<>();

        for(SpawnFilter manager : from.getSpawnFilters()) {
            //spawnManagers.add(manager.name);
        }

        for(Round round : from.getRounds()) {
            rounds.add(new RoundData(round));
        }
    }
}
