package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.mob.Mob;

import java.util.*;

import io.github.zap.zombiesplugin.mob.MobInfo;
import io.github.zap.zombiesplugin.utils.RandomIterator;
import org.bukkit.Location;

public class RangedSpawnPointManager extends SpawnPointManager {

	/**
	 * The Game manager
	 */
	private final GameManager gameManager;

	/**
	 * The maximum range from a player the spawn point manager spawns mobs from
	 */
	private final int spawnRange;

	/**
	 * Creates the spawn point manager
	 *
	 * @param acceptedMobTypes The list of possible mob types the spawn points can spawn
	 * @param coordinates      The list of spawn points the manager should manage
	 */
	public RangedSpawnPointManager(GameManager gameManager, HashSet<MobInfo> acceptedMobTypes, Location[] coordinates, int spawnRange) {
		super(gameManager, acceptedMobTypes, coordinates);
		this.gameManager = gameManager;
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
					for(Mob mob : mobs) {
						if(acceptedMobTypes.contains(mob.getMobInfo())) {
							mobs.remove(i);
							spawnPoint.spawn(mob);
							break;
						}
						i++;
					}
					return;
				}
			}
		}
	}
}
