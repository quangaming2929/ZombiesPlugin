package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.utils.RandomIterator;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class RandomizedSpawnFilter extends SpawnFilter {
    @Override
    public void spawn(GameManager manager, ArrayList<MythicMob> mobs, ArrayList<Room> rooms) {
        for(Room room : rooms) {
            Collections.shuffle(room.getSpawnPoints());
        }

        Collections.shuffle(rooms);
        super.spawn(manager, mobs, rooms);
    }
}
