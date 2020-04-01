package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.data.SpawnManagerData;
import io.github.zap.zombiesplugin.utils.RandomIterator;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.util.ArrayList;
import java.util.HashSet;

public class RandomizedSpawnManager extends SpawnManager {
    public RandomizedSpawnManager(String name, GameMap map, HashSet<MythicMob> acceptedMobTypes) {
        super(name,"randomized",  map, acceptedMobTypes);
    }

    public RandomizedSpawnManager(SpawnManagerData data) {
        super(data.name, data.typeConverter, ZombiesPlugin.instance.getMap(data.mapName), new HashSet<>());

        for(String name : data.mobNames) {
            acceptedMobTypes.add(MythicMobs.inst().getAPIHelper().getMythicMob(name));
        }
    }

    @Override
    public void spawn(GameManager manager, ArrayList<MythicMob> mobs) {
        super.spawn(manager, new RandomIterator<>(spawnPoints), mobs);
    }
}
