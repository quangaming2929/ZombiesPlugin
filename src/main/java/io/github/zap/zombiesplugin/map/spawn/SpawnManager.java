package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.utils.ResettableIterator;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
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
		this.map = map;
		this.acceptedMobTypes = acceptedMobTypes;
		spawnPoints = new ArrayList<>();
	}

	protected void spawn(ResettableIterator<SpawnPoint> spawnPoints, ArrayList<MythicMob> mobs) {
		for(int i = mobs.size() - 1; i >= 0 ; i--) {
			MythicMob mob = mobs.get(i);
			if(acceptedMobTypes.contains(mob)) {
				while(spawnPoints.hasNext()) {
					SpawnPoint point = spawnPoints.next();
					if(point.canSpawn()) {
						point.spawn(mob);
						mobs.remove(i);
					}
					if(mobs.size() == 0) return;
				}
				spawnPoints.reset();
			}
		}
	}

	public abstract void spawn(ArrayList<MythicMob> mobs);

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