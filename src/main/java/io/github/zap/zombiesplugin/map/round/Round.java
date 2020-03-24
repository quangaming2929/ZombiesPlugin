package io.github.zap.zombiesplugin.map.round;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.spawn.SpawnPointManager;
import io.github.zap.zombiesplugin.mob.CustomMob;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Round {

	private GameManager gameManager;

	/**
	 * The waves in the round
	 */
	private Wave[] waves;

	public Round(GameManager gameManager, Wave[] waves) {
		this.gameManager = gameManager;
		this.waves = waves;
	}

	public void startRound() {
		long accumulatedDelay = 0;

		for (Wave wave : waves) {
			accumulatedDelay += wave.getDelay();

			ArrayList<CustomMob> mobs = wave.getMobs();
			new BukkitRunnable() {
				@Override
				public void run() {
					for (SpawnPointManager spawnPointManager : gameManager.getMap().getSpawnPointManagers()) {
						spawnPointManager.spawn(mobs);
					}
				}

			}.runTaskLater(ZombiesPlugin.instance, accumulatedDelay);
		}
	}
}