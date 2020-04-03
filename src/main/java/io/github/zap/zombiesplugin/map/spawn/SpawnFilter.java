package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.map.Window;
import io.github.zap.zombiesplugin.utils.ResettableIterator;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class SpawnFilter {
	protected final HashSet<MythicMob> acceptedMobTypes;

	public SpawnFilter() {
		acceptedMobTypes = new HashSet<>();
	}

	public SpawnFilter(HashSet<MythicMob> types) {
		acceptedMobTypes = types;
	}

	public void spawn(GameManager manager, ArrayList<MythicMob> mobs, ArrayList<Room> rooms) {
		ArrayList<SpawnPoint> spawns = new ArrayList<>();
		for(Room testRoom : rooms) {
			if(testRoom.isOpen()) spawns.addAll(testRoom.getSpawnPoints());
		}

		for(int i = mobs.size() - 1; i >= 0 ; i--) {
			MythicMob mob = mobs.get(i);
			for(SpawnPoint spawnPoint : spawns) {
				if(spawnPoint.canSpawn()) {
					if(acceptedMobTypes.contains(mob)) {
						mobs.remove(i);
						spawnPoint.spawn(manager, mob);

						if(mobs.size() == 0) return;
					}
				}
			}
		}
	}

	public void add(MythicMob mob) {
		acceptedMobTypes.add(mob);
	}

	public HashSet<MythicMob> getAcceptedMobTypes() { return acceptedMobTypes; }
}