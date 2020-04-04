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
		for(int i = mobs.size() - 1; i >= 0 ; i--) {
			MythicMob mob = mobs.get(i);
			for(SpawnPoint spawnPoint : spawnPoints) {
				if(acceptedMobTypes.contains(mob)) {
					mobs.remove(i);
					spawnPoint.spawn(manager, mob);

					if(mobs.size() == 0) return;
				}
			}
		}
	}

	public void add(MythicMob mob) {
		acceptedMobTypes.add(mob);
	}

	public HashSet<MythicMob> getAcceptedMobTypes() { return acceptedMobTypes; }
}