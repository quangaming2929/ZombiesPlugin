package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.Map;
import io.github.zap.zombiesplugin.mob.Mob;

import io.github.zap.zombiesplugin.mob.MobInfo;
import io.github.zap.zombiesplugin.utils.RandomIterator;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashSet;

public class RangedSpawnPointManager extends SpawnPointManager {
	/**
	 * The maximum range from a player the spawn point manager spawns mobs from
	 */
	private final int spawnRange;

	/**
	 * Creates the spawn point manager
	 *
	 * @param acceptedMobTypes The list of possible mob types the spawn points can spawn
	 */
	public RangedSpawnPointManager(GameManager manager, HashSet<MobInfo> acceptedMobTypes, int spawnRange) {
		super(manager, acceptedMobTypes);
		this.spawnRange = spawnRange;
	}

	@Override
	public boolean isValid() {
		//TODO: check to see if the room to which this spawnpoint belongs is unlocked
		return true;
	}

	@Override
	public void spawn(ArrayList<Mob> mobs) {
		if(!isValid()) return;

		while(true) {
			for(SpawnPoint spawnPoint : spawnPoints) {
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
		}
	}
}
