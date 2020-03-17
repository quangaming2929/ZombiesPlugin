package io.github.zap.zombiesplugin.round;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.spawn.SpawnPointManager;
import org.bukkit.scheduler.BukkitRunnable;

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

			new BukkitRunnable() {
				@Override
				public void run() {
					for (SpawnPointManager spawnPointManager : gameManager.getMap().getSpawnPointManagers()) {
						spawnPointManager.spawn(waves[0].getMobs());
					}
				}

			}.runTaskLater(ZombiesPlugin.instance, accumulatedDelay);
		}
	}
}
