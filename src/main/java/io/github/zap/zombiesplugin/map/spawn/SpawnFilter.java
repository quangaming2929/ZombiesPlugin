package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.util.*;

public class SpawnFilter {
	protected final HashSet<MythicMob> acceptedMobTypes;

	public SpawnFilter() {
		acceptedMobTypes = new HashSet<>();
	}

	public SpawnFilter(HashSet<MythicMob> types) {
		acceptedMobTypes = types;
	}

	public void spawn(GameManager manager, ArrayList<MythicMob> mobs, ArrayList<SpawnPoint> spawnPoints) {
		int i = mobs.size() - 1;
		int j = 0;
		for(;i >= 0; i--) {
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

	public HashSet<MythicMob> getAcceptedMobTypes() { return acceptedMobTypes; }
}