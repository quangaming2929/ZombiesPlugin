package io.github.zap.zombiesplugin.map.round;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameDifficulty;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.spawn.SpawnFilter;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Round {
	private String mapName;
	private final ArrayList<Wave> waves;

	private GameMap map;

	public Round(String mapName, ArrayList<Wave> waves) {
		this.mapName = mapName;
		this.waves = waves;

		map = ZombiesPlugin.instance.getMap(mapName);
	}

	public void startRound(GameManager manager, GameDifficulty difficulty) {
		long accumulatedDelay = 0;
		for (Wave wave : waves) {
			accumulatedDelay += wave.getDelay(difficulty);

			new BukkitRunnable() {
				@Override
				public void run() {
					ArrayList<MythicMob> mobs = wave.getMobs(difficulty);
					for (SpawnFilter spawnFilter : map.getSpawnFilters()) {
						spawnFilter.spawn(manager, mobs, map.getRooms());
					}
				}

			}.runTaskLater(ZombiesPlugin.instance, accumulatedDelay);
		}
	}

	public GameMap getMap() { return ZombiesPlugin.instance.getMap(mapName); }

	public ArrayList<Wave> getWaves() { return waves; }
}