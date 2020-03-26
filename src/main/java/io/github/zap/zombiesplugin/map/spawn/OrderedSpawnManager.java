package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.GameMap;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.util.ArrayList;
import java.util.HashSet;

public class OrderedSpawnManager extends SpawnManager {
	public OrderedSpawnManager(String name, GameMap map, HashSet<MythicMob> acceptedMobTypes) {
		super(name, "ordered", map, acceptedMobTypes);
	}

	@Override
	public void spawn(GameManager game, ArrayList<MythicMob> mobs) {
		super.spawn(game, spawnPoints.iterator(), mobs);
	}
}
