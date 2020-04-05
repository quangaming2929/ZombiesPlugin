package io.github.zap.zombiesplugin.map.round;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameDifficulty;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.map.spawn.SpawnFilter;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.Difficulty;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Round {
	private final ArrayList<Wave> waves;
	private int totalMobCount = 0;

	public Round(ArrayList<Wave> waves) {
		this.waves = waves;
	}

	public void start(GameManager manager) {
		long accumulatedDelay = 0;
		GameMap map = manager.getSettings().getGameMap();
		GameDifficulty difficulty = manager.getSettings().getDifficulty();
		for (Wave wave : waves) {
			accumulatedDelay += wave.getDelay(difficulty);

			new BukkitRunnable() {
				@Override
				public void run() {
					ArrayList<MythicMob> mobs = wave.getMobs(difficulty);
					for (SpawnFilter spawnFilter : map.getSpawnFilters()) {
						ArrayList<SpawnPoint> available = new ArrayList<>();
						for(Room room : map.getRooms()) {
							if(room.isOpen()) {
								available.addAll(room.getSpawnPoints());
								room.getWindows().forEach(window -> available.add(window.getSpawnPoint()));
							}
						}

						spawnFilter.spawn(manager, mobs, available);
					}
				}

			}.runTaskLater(ZombiesPlugin.instance, accumulatedDelay);
		}
	}

	public ArrayList<Wave> getWaves() { return waves; }
}