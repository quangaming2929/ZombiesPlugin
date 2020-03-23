package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.Map;
import io.github.zap.zombiesplugin.mob.Mob;
import io.github.zap.zombiesplugin.mob.MobInfo;
import io.github.zap.zombiesplugin.utils.RandomIterator;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class RandomizedSpawnPointManager extends SpawnPointManager {
    public RandomizedSpawnPointManager(GameManager manager, HashSet<MobInfo> acceptedMobTypes) {
        super(manager, acceptedMobTypes);
    }

    @Override
    public void spawn(ArrayList<Mob> mobs) {
        if(!isValid()) return;

        RandomIterator<SpawnPoint> iterator = new RandomIterator<>(spawnPoints);
        while(true) {
            while(iterator.hasNext()) {
                SpawnPoint spawnPoint = iterator.next();
                if(spawnPoint.isAvailable()) {
                    int i = 0;
                    boolean added = false;
                    for(Mob mob : mobs) {
                        if(acceptedMobTypes.contains(mob.getMobInfo())) {
                            mobs.remove(i);
                            spawnPoint.spawn(mob);
                            added = true;
                            break;
                        }
                        i++;
                    }
                    if(!added || mobs.size() == 0) return;
                }
            }
            iterator.reset();
        }
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
