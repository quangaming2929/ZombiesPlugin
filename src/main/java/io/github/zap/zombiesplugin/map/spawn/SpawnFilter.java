package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.ISpawnpointContainer;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.util.*;

public class SpawnFilter {
	protected final HashSet<MythicMob> acceptedMobTypes;
	protected final ArrayList<SpawnPoint> spawnPoints;

	public SpawnFilter(HashSet<MythicMob> mobs, ArrayList<ISpawnpointContainer> spawnPoints) {
		acceptedMobTypes = mobs;
		this.spawnPoints = new ArrayList<>();

		for(ISpawnpointContainer container : spawnPoints) {
			this.spawnPoints.add(container.getSpawnpoint());
		}
	}

	public SpawnFilter(ArrayList<ISpawnpointContainer> spawnPoints) {
		this(new HashSet<>(), spawnPoints);
	}

	public SpawnFilter(HashSet<MythicMob> mobs) {
		this(mobs, new ArrayList<>());
	}

	public SpawnFilter() {
		this(new HashSet<>(), new ArrayList<>());
	}

	public void spawn(GameManager manager, ArrayList<MythicMob> mobs) {
		int i = mobs.size() - 1;
		int j = 0;

		for(; i >= 0; i--) {
			MythicMob sample = mobs.get(i);
			if(acceptedMobTypes.contains(sample)) {
				mobs.remove(i);
				spawnPoints.get(j).spawn(manager, sample);

				j++;
				j %= spawnPoints.size();
			}
		}
	}

	public void add(MythicMob mob) {
		acceptedMobTypes.add(mob);
	}

	public void add(ISpawnpointContainer container) {
		spawnPoints.add(container.getSpawnpoint());
	}

	public ArrayList<SpawnPoint> getSpawnpoints() { return spawnPoints; }

	public HashSet<MythicMob> getAcceptedMobTypes() { return acceptedMobTypes; }
}