package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.utils.ResettableIterator;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class OrderedSpawnManager extends SpawnManager {
	public OrderedSpawnManager(String name, GameMap map, HashSet<MythicMob> acceptedMobTypes) {
		super(name, "ordered", map, acceptedMobTypes);
	}

	@Override
	public void spawn(ArrayList<MythicMob> mobs) {
		super.spawn(new ResettableIterator<SpawnPoint>() {
			private Iterator<SpawnPoint> iterator;

			@Override
			public boolean hasNext() {
				if(iterator == null) {
					iterator = spawnPoints.iterator();
				}

				return iterator.hasNext();
			}

			@Override
			public SpawnPoint next() {
				if(iterator == null) {
					iterator = spawnPoints.iterator();
				}

				return iterator.next();
			}

			@Override
			public void remove() {
				if(iterator == null) {
					iterator = spawnPoints.iterator();
				}

				iterator.remove();
			}

			@Override
			public void reset() {
				iterator = null;
			}
		}, mobs);
	}
}
