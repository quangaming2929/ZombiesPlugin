package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.utils.RandomIterator;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.util.ArrayList;
import java.util.HashSet;

public class RandomizedSpawnManager extends SpawnManager {
    public RandomizedSpawnManager(String name, GameMap map, HashSet<MythicMob> acceptedMobTypes) {
        super(name,"randomized",  map, acceptedMobTypes);
    }

    @Override
    public void spawn(ArrayList<MythicMob> mobs) {
        super.spawn(new RandomIterator<>(spawnPoints), mobs);
    }
}
