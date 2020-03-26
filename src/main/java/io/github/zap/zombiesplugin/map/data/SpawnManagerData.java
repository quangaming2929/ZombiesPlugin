package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.map.spawn.SpawnManager;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.lumine.xikage.mythicmobs.mobs.*;

import java.util.ArrayList;
import java.util.HashSet;

public class SpawnManagerData {
    public String name;
    public String typeConverter;
    public String mapName;
    public HashSet<String> mobNames;
    public ArrayList<SpawnPointData> spawnPoints;

    public SpawnManagerData() {}

    public SpawnManagerData(SpawnManager from) {
        this.name = from.name;
        this.typeConverter = from.typeConverter;
        this.mapName = from.getMap().name;
        this.mobNames = new HashSet<>();
        this.spawnPoints = new ArrayList<>();

        for(MythicMob mob : from.getAcceptedMobTypes()) {
            mobNames.add(mob.getInternalName());
        }

        for(SpawnPoint point : from.getSpawnPoints()) {
            spawnPoints.add(new SpawnPointData(point));
        }
    }
}
