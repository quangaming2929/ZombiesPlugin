package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.GameMap;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public abstract class SpawnManager {
	public final String name;
	public final String typeConverter;
	protected final GameMap map;
	protected final HashSet<MythicMob> acceptedMobTypes;
	protected final ArrayList<SpawnPoint> spawnPoints;

	public SpawnManager(String name, String typeConverter, GameMap map, HashSet<MythicMob> acceptedMobTypes) {
		this.name = name;
		this.typeConverter = typeConverter;
		this.acceptedMobTypes = acceptedMobTypes;
		spawnPoints = new ArrayList<>();
		this.map = map;
	}

	protected void spawn(Iterator<SpawnPoint> spawnPoints, ArrayList<MythicMob> mobs) {
		while(true) {
			while(spawnPoints.hasNext()) {
				SpawnPoint spawnPoint = spawnPoints.next();
				if(spawnPoint.canSpawn()) {
					int i = 0;
					boolean spawned = false;
					for(MythicMob mob : mobs) {
						if(acceptedMobTypes.contains(mob)) {
							mobs.remove(i);
							spawnPoint.spawn(mob);
							spawned = true;
							break;
						}
						i++;
					}
					if(!spawned || mobs.size() == 0) return;
				}
				else spawnPoints.remove();
			}
		}
	}

	public abstract void spawn(GameManager game, ArrayList<MythicMob> mobs);

	public void add(SpawnPoint spawnPoint) {
		spawnPoints.add(spawnPoint);
	}

	public void add(MythicMob mob) {
		acceptedMobTypes.add(mob);
	}

	public GameMap getMap() { return map; }

	public HashSet<MythicMob> getAcceptedMobTypes() { return acceptedMobTypes; }

	public ArrayList<SpawnPoint> getSpawnPoints() { return spawnPoints; }
}