package io.github.zap.zombiesplugin.map.round;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.spawn.SpawnManager;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Map;

public class Round {
	private final GameMap gameMap;
	private final ArrayList<Wave> waves;

	public Round(GameMap map, ArrayList<Wave> waves) {
		gameMap = map;
		this.waves = waves;
	}

	public void startRound() {
		long accumulatedDelay = 0;

		for (Wave wave : waves) {
			accumulatedDelay += wave.getDelay();

			ArrayList<MythicMob> mobs = wave.getMobs();
			new BukkitRunnable() {
				@Override
				public void run() {
					for (SpawnManager spawnManager : gameMap.getSpawnManagers()) {
						spawnManager.spawn(mobs);
					}
				}

			}.runTaskLater(ZombiesPlugin.instance, accumulatedDelay);
		}
	}

	public GameMap getMap() {return gameMap;}

	public ArrayList<Wave> getWaves() {return waves;}
}